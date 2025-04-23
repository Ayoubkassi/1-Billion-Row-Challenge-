const fs = require('fs');
const readline = require('readline');

async function measureTime(callback) {
  const start = process.hrtime.bigint();
  await callback();
  const end = process.hrtime.bigint();
  const duration = Number(end - start) / 1_000_000_000; 
  console.log(`Temps écoulé: ${duration.toFixed(2)} secondes`);
}

measureTime(async () => {
  const fileStream = fs.createReadStream('../ten_million_data.csv');

  const rl = readline.createInterface({
    input: fileStream,
    crlfDelay: Infinity
  });

  const cityPrices = {};
  const productPrices = {};

  for await (const line of rl) {
    const [city, product, price] = line.split(',');
    const parsedPrice = parseFloat(price);

    if (!cityPrices[city]) {
      cityPrices[city] = 0;
    }
    cityPrices[city] += parsedPrice;

    if (!productPrices[product] || parsedPrice < productPrices[product]) {
      productPrices[product] = parsedPrice;
    }
  }

  let cheapestCity = null;
  let cheapestCityPrice = Infinity;
  for (const city in cityPrices) {
    if (cityPrices[city] < cheapestCityPrice) {
      cheapestCity = city;
      cheapestCityPrice = cityPrices[city];
    }
  }

  const cheapestProducts = Object.entries(productPrices)
    .sort((a, b) => a[1] - b[1])
    .slice(0, 5);

  console.log(`Ville la moins chère: ${cheapestCity} ${cheapestCityPrice.toFixed(2)}`);
  console.log('5 produits les moins chers:');
  cheapestProducts.forEach(([product, price]) => {
    console.log(`${product} ${price.toFixed(2)}`);
  });
});
