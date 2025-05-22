# Electra Challenge — Trouve la ville la moins chère ! 🇫🇷

Bienvenue au **Electra Challenge**, un défi de programmation conçu pour tester vos compétences en codage et votre capacité à écrire du code optimisé et performant. L'objectif est de développer un programme capable de traiter un fichier contenant jusqu'à **1 milliard de lignes**, afin de déterminer **la ville française la moins chère** selon les prix des produits.

## 🧠 Le Défi : La Ville la Moins Chère

Nous sommes en 2025. Entre guerre et inflation mondiale, le coût de la vie a explosé. En France, **Henri**, développeur freelance, cherche à s’installer dans une ville où la vie est plus abordable. Il dispose d’un gigantesque fichier de données sur les prix des produits dans **101 villes françaises**. C’est là que vous intervenez !

### 🧾 Hypothèse importante

Chaque **produit existe dans toutes les villes**, mais **à des prix différents**. Par exemple, **Paris peut proposer le même produit plusieurs fois avec des prix différents** (par exemple 4 laits avec des prix variés). **Pour chaque ville, on ne retient que le prix minimum par produit** afin de calculer le coût total de la vie dans cette ville.

---

## 🎯 Objectifs du Programme

Votre mission est de créer un programme qui :

1. **Lit un fichier de 1 milliard de lignes (`one_million_data.csv`)** : Le fichier contient des données sur les prix des produits dans différentes villes.
2. **Calcule le coût total de la vie pour chaque ville** : Pour chaque ville, retenez **le prix minimum pour chaque produit**. Ensuite, **additionnez ces prix minimaux** pour obtenir le coût total de la ville.
3. **Trouve la ville la moins chère** : Identifiez la ville avec le coût total minimal.
4. **Identifie les 5 produits les moins chers (sans doublons)** dans le fichier, toutes villes confondues.

---

## 👨‍💻 Langages Acceptés

Vous pouvez soumettre votre code dans l’un des langages suivants :
- C
- C++
- Java
- Rust
- Python
- PHP
- JavaScript

C’est le moment idéal pour prouver que votre langage préféré est le plus rapide et le plus efficace ! 💪

---

## 🧪 Instructions

1. **Téléchargez le fichier `one_million_data.csv`** : Assurez-vous d'avoir le fichier de données prêt pour le traitement.
2. **Développez votre programme** : Écrivez le code dans le langage de votre choix pour répondre aux objectifs mentionnés.
3. **Testez votre solution** : Vérifiez que votre programme fonctionne correctement avec le fichier de données.
4. **Soumettez votre code** : Partagez votre solution pour évaluation.

---

## 📁 Exemple de Format de Données

✅ INPUT :

Paris,tomato,6.23  
Paris,tomato,7.23  
Paris,tomato,8.23  
Paris,tomato,9.23  
Paris,potato,4.21  
Paris,flour,6.24  
Paris,oil,9.24  
Paris,oil,9.94  
Paris,oil,8.24  
Paris,sugar,5.23  
Paris,sugar,5.23  
Paris,sugar,5.23  
Paris,gaz,9.25  
Lyon,tomato,4.5  
Lyon,tomato,3.5  
Lyon,potato,3.5  
Lyon,flour,5.2  
Lyon,oil,7.7  
Lyon,oil,6.7  
Lyon,sugar,4.5  
Lyon,gaz,9.3  
Lyon,gaz,2.3  
Lyon,gaz,1.3  
Marseille,tomato,5.85  
Marseille,tomato,9.85  
Marseille,potato,4.65  
Marseille,flour,5.65  
Marseille,oil,8.15  
Marseille,oil,9.15  
Marseille,oil,7.15  
Marseille,sugar,9.05  
Marseille,sugar,9.15  
Marseille,gaz,9.45  

✅ OUTPUT :

Lyon 48.50  
gaz 1.30  
potato 3.50  
tomato 3.50  
sugar 4.50  
flour 5.20  
