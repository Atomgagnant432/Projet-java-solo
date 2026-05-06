# Simulateur de lancement spatial (Java console)

Projet scolaire Java (B1) : application en console permettant de configurer une fusée, choisir une mission et simuler un lancement (succès/échec + raison), avec calcul du carburant et du coût total.

## Prérequis
- Java JDK 17+ (ou au minimum un `javac` disponible dans le `PATH`)
- Windows / PowerShell (les commandes ci-dessous sont données pour PowerShell)

## Compilation
Depuis la racine du projet :

```powershell
javac (Get-ChildItem -Recurse -Filter *.java | % FullName)
```

## Exécution

```powershell
java internal.Main
```

## Utilisation
Le menu principal propose :
- **1) Choisir une mission** : Orbite terrestre, ISS, Lune, Mars, Uranus
- **2) Configurer la fusée** : choix du lanceur + capsule + boosters (limite du lanceur) avec affichage des poids/capacités pour éviter une surcharge
- **3) Lancer la simulation** : vérifications + aléa + affichage des statistiques et du résultat

## Règles métier (résumé)
- Les **prix** sont exprimés en **M€**.
- **Carburant nécessaire (tonnes)** :
  - `carburant = (masse_totale * distance * coefficient_mission) / 1000`
- **Coût total d’un lancement (M€)** :
  - `cout_total = prix_fusee + (carburant * KEROSENE_PRICE_PER_TON)`
  - `KEROSENE_PRICE_PER_TON = 1200`
- **Échec (si applicable)** :
  - Carburant nécessaire > capacité carburant du lanceur → « Carburant insuffisant »
  - Masse totale > charge utile du lanceur → « Surcharge dépassée »
  - Mission habitée + capsule non habitée → « Capsule incompatible… »
- **Aléa** :
  - Tirage `Math.random()` ; si `< 0.05` → échec « Anomalie technique imprévue »

## Structure du code (arborescence)
```text
.
├─ README.md
└─ internal/
   ├─ Main.java
   ├─ start.java
   ├─ Menu/
   │  └─ Menu.java
   ├─ rocket/
   │  └─ rocket.java
   ├─ missions/
   │  ├─ missions.java
   │  ├─ earthOrbite.java
   │  ├─ Iss.java
   │  ├─ moon.java
   │  ├─ mars.java
   │  └─ uranus.java
   ├─ SpaceLauncher/
   │  ├─ spacelauncher.java
   │  ├─ ariane5.java
   │  ├─ falcon9.java
   │  ├─ saturnV.java
   │  └─ sls.java
   ├─ capsules/
   │  ├─ capsule.java
   │  ├─ apollo.java
   │  ├─ crewdragon.java
   │  ├─ cargodragon.java
   │  └─ orion.java
   └─ boosters/
      ├─ Booster.java
      ├─ BE3.java
      ├─ EAP.java
      └─ SRB.java
```

## Notes / limites actuelles
- L’historique persistant (fichier) n’est pas encore implémenté.
- La « mission personnelle » n’est pas encore ajoutée.