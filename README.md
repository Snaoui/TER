TER du Master 1 informatique de l'université de Luminy.

Sujet : QCM Mix

Auteur : Mehdi SNAOUI



# dépendances
- production:
  - Java 8
  - LibreOffice/OpenOffice
  - Python 3 (Linux et OS X)
  - JavaFX, si ça build/run pas et qu'il arrive pas à importer javafx, ça doit venir de là
      - Si JavaFX n'est pas installé, une des manières de faire est de télécharger le dernier JDK via oracle.com, décompresser l'archive et de faire pointer les variables d'environnement JAVA_HOME et JFXRT_HOME vers le répertoire créé.

- dev
  - gradle > 2.0 (il faut avoir la commande `gradle`)
      - si vous ne pouvez pas l'installer, utiliser le gradle "intégré" au projet. Taper `./gradlew` à place de `gradle` dans le commandes.

# setup du projet
`gradle test` doit marcher, sinon cherchez ce qui ne va pas et demandez de l'aide si vous trouvez pas.


## lancer en ligne de commande
`gradle run`


## importer dans Eclipse
- `gradle eclipse` pour générer les fichiers de projet Eclipse
- dans eclipse "import existing project"


# livrer le logiciel
Après un `gradle build`, les fichiers se trouvent dans `build/distributions/QCMix`. La phase `jfxDeploy` peut prendre 2 min.
Pour Windows, donner le dossier `app` et faire un raccouris vers `QCMix.jar`
Y copier unoconv.py dans le dossier `app`

# Documentation
Voir le [wiki](https://github.com/Snaoui/TER/wikis/home) et la Javadoc dans `/doc`.

# License
This program is released under the GNU GPL version 3 or any later version (GPLv3+)
