package java_rmi_tp1_client.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;

import java_rmi_tp1_communs.classes.DossierDeSuivi;
import java_rmi_tp1_communs.classes.Espece;
import java_rmi_tp1_communs.interfaces.Animal;
import java_rmi_tp1_communs.interfaces.CabinetVeterinaire;
import java_rmi_tp1_communs.interfaces.IClient;

@SuppressWarnings("serial")
public class Client extends UnicastRemoteObject implements IClient {

	public Client() throws RemoteException {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	 // Méthode pour afficher les notifications
    public void recevoirNotification(String message) throws RemoteException {
        System.out.println("Alerte reçue : " + message);
    }

	public static void printAnimalInfosDateVaccination(Animal animal) throws RemoteException {
		System.out.println("Animal trouvé: " + animal.printAnimalInfos());
		System.out.println("Dossier de suivi : " + animal.getDateVaccination());
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in); // Un objet scanner pour lire l'entree du clavier

		String host = (args.length < 1) ? null : args[0];
		try {
			Registry registry = LocateRegistry.getRegistry(host);
//			Animal stub = (Animal) registry.lookup("Animal");
			CabinetVeterinaire stub = (CabinetVeterinaire) registry.lookup("cabinetObj");
			Client client = new Client();
            stub.enregistrerObservateur(client);
			System.out.println(
					"Lister tous les annimaux ou Ajouter ou  chercher un animal : veuillez saisir 'show all animals' pour lister tous les animaux, 'Add' pour ajouter et 'search' pour chercher : ");
			String action = scanner.nextLine().toLowerCase();
			if (action.equals("Add".toLowerCase())) {
				System.out.println("Entrez le nom de l'animal : ");
	            String nom = scanner.nextLine();
	            System.out.println("Entrez le nom du maître : ");
	            String nomMaitre = scanner.nextLine();
	            System.out.println("Entrez l'espèce : ");
	            String espece = scanner.nextLine();
	            System.out.println("Entrez la durée de vie moyenne : ");
	            int durreDeVieMoy = scanner.nextInt();
	            scanner.nextLine();
	            Espece especeDog = new Dog(espece, durreDeVieMoy);
	            System.out.println("Entrez la race : ");
	            String race = scanner.nextLine();
	            System.out.println("Date de vaccination (jj-mm-aaaa) : ");
	            String dateVaccinationAdd = scanner.nextLine();
	            stub.ajouterAnimal(nom, nomMaitre, especeDog, race, new DossierDeSuivi(dateVaccinationAdd));
	            System.out.println("Animal ajouté avec succès.");
			} else if (action.equals("show all animals".toLowerCase())) {
				ArrayList<Animal> tousLesAnimaux = stub.listerAnimaux();
				for (Animal animal : tousLesAnimaux) {
				    System.out.println(animal.printAnimalInfos());
				}
				System.out.println(tousLesAnimaux.size());
			}
			else if (action.equals("search".toLowerCase())) {
				// Demander a l'utilisateur de saisir le nom de l'animal a rechercher
				System.out.println("Entrez le nom de l'animal à rechercher : ");
				String nomAnimal = scanner.nextLine().toLowerCase(); // Lire l'entree du clavier

				// Recherche d'un animal par nom
				Animal animal = stub.chercherAnimalParNom(nomAnimal);
				if (animal != null) {
					printAnimalInfosDateVaccination(animal);
					System.out.println("Voudriez-vous modifier le dossier de suivi (date vaccination) ? ");
					String response = scanner.nextLine().toLowerCase(); // Lire l'entree du clavier
					if (response.equals("O".toLowerCase()) || response.equals("Oui".toLowerCase())) {
						System.out.println("Entrez la date de la prochaine vaccination : ");
						String dateVaccination = scanner.nextLine().toLowerCase(); // Lire l'entree du clavier
						animal.setDateVaccination(dateVaccination);
						printAnimalInfosDateVaccination(animal);
					} else {
						scanner.close();
					}
				} else {
					System.out.println("Aucun animal trouvé avec le nom '" + nomAnimal + "'");
				}
			} else {
				scanner.close();
			}
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
	}

}
