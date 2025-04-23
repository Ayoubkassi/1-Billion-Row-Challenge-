package main

import (
	"fmt"
	"math/rand"
	"os"
	"time"
)

var cities = []string{
	"Paris", "Marseille", "Lyon", "Toulouse", "Nice", "Nantes", "Montpellier", "Strasbourg", "Bordeaux", "Lille",
	"Rennes", "Reims", "Le Havre", "Saint-Étienne", "Toulon", "Grenoble", "Dijon", "Angers", "Nîmes", "Villeurbanne",
	"Le Mans", "Aix-en-Provence", "Clermont-Ferrand", "Brest", "Tours", "Amiens", "Limoges", "Annecy", "Boulogne-Billancourt", "Perpignan",
	"Metz", "Besançon", "Orléans", "Mulhouse", "Rouen", "Caen", "Saint-Denis", "Argenteuil", "Montreuil", "Nancy",
	"Roubaix", "Tourcoing", "Nanterre", "Vitry-sur-Seine", "Créteil", "Avignon", "Dunkerque", "Poitiers", "Asnières-sur-Seine", "Courbevoie",
	"Versailles", "Colombes", "Aulnay-sous-Bois", "Saint-Pierre", "Douai", "Fort-de-France", "Rueil-Malmaison", "Pau", "Aubervilliers", "Champigny-sur-Marne",
	"La Rochelle", "Calais", "Saint-Maur-des-Fossés", "Cannes", "Antibes", "Drancy", "Ajaccio", "Mérignac", "Saint-Nazaire", "Colmar",
	"Issy-les-Moulineaux", "Noisy-le-Grand", "Évry-Courcouronnes", "Valence", "Lorient", "Levallois-Perret", "Troyes", "Neuilly-sur-Seine", "Vénissieux", "Antony",
	"Hyères", "Ivry-sur-Seine", "Montauban", "Mantes-la-Jolie", "Cergy", "La Seyne-sur-Mer", "Pessac", "Chambéry", "Clichy", "Beauvais",
	"Cholet", "Cayenne", "Saint-Quentin", "Pantin", "Belfort", "Laval", "Meaux", "Vannes", "Sevran", "Narbonne",
}

var products = []string{
	"tomate", "pomme de terre", "farine", "huile", "sucre", "gaz", "pain", "lait", "fromage", "œufs",
	"poulet", "bœuf", "porc", "poisson", "riz", "pâtes", "haricots", "carottes", "oignons", "ail",
	"bananes", "pommes", "oranges", "raisins", "fraises", "citrons", "ananas", "kiwis", "mangues", "pêches",
	"chocolat", "café", "thé", "jus d'orange", "soda", "eau", "bière", "vin", "beurre", "yaourt",
	"miel", "confiture", "sel", "poivre", "épices", "sauce tomate", "moutarde", "ketchup", "mayonnaise", "vinaigre",
	"champignons", "épinards", "brocoli", "chou-fleur", "courgettes", "aubergines", "concombres", "poivrons", "betteraves", "radis",
	"noix", "amandes", "noisettes", "cacahuètes", "pistaches", "graines de tournesol", "graines de chia", "graines de lin", "avoine", "maïs",
	"lentilles", "pois chiches", "haricots rouges", "haricots noirs", "quinoa", "couscous", "boulgour", "polenta", "tapioca", "semoule",
	"saumon", "thon", "crevettes", "moules", "calamars", "sardines", "maquereau", "crabe", "homard", "huîtres",
	"agneau", "dinde", "canard", "lapin", "veau", "jambon", "saucisses", "bacon", "pâté", "foie gras",
	"choucroute", "ratatouille", "cassoulet", "bouillabaisse", "fondue", "raclette", "tartiflette", "quiche", "pizza", "tacos",
	"sushi", "ramen", "curry", "kebab", "falafel", "hummus", "taboulé", "couscous", "paella", "lasagnes",
}

func main() {
	rand.Seed(time.Now().UnixNano())

	file, err := os.Create("final_data.csv")
	if err != nil {
		fmt.Println("Error while create file:", err)
		return
	}
	defer file.Close()

	const numRows = 1_000_000_000

	for i := 0; i < numRows; i++ {
		city := cities[rand.Intn(len(cities))]
		product := products[rand.Intn(len(products))]
		price := fmt.Sprintf("%.2f", rand.Float64()*9.9+0.1) 
		line := fmt.Sprintf("%s,%s,%s\n", city, product, price)
		_, err := file.WriteString(line)
		if err != nil {
			fmt.Println("Error while write into file:", err)
			return
		}
	}

	fmt.Println("File generated successfully!")
}
