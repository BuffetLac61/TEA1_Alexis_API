package Test

import com.example.tea1_v01.ItemToDo
import com.example.tea1_v01.ListeToDo
import com.example.tea1_v01.ProfilListeToDo
import org.junit.Test

class ProfilListeToDoTest {
    @Test
    fun ajouterListe_test() {
        // Création d'une instance de ProfilListeToDo
        val profil = ProfilListeToDo("monlogin")

        // Création d'une liste de tâches
        var liste = ListeToDo()
        liste.setTitreListeToDo("Ma liste de tâches")

        //creation des tâches

        var item1 = ItemToDo("Faire la vaisselle")
        var item2 = ItemToDo("Faire la vaisselle encore")
        var item3 = ItemToDo("reFaire la vaisselle")
        var items = arrayOf(item1, item2, item3)
        liste.setLesItems(items)

        // Ajout de la liste à l'instance de ProfilListeToDo
        profil.ajouterListe(liste)

        liste = ListeToDo()
        liste.setTitreListeToDo("Mon autre liste de tâches")

        //creation des tâches
        item1 = ItemToDo("Faire la vaisselle")
        item2 = ItemToDo("Faire la vaisselle encore")
        item3 = ItemToDo("reFaire la vaisselle")
        items = arrayOf(item1, item2, item3)

        liste.setLesItems(items)

        // Ajout de la liste à l'instance de ProfilListeToDo
        profil.ajouterListe(liste)

        //On marque un item comme fait
        val itemModified = profil.getMesListesToDo()[0].rechercherItem("Faire la vaisselle")

        if (itemModified != null) {
            itemModified.setFait(true)
        }

        // Vérification que la liste a été ajoutée avec succès
        println( profil.toString())

    }


}
