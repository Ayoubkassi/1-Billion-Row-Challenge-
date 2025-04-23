require 'csv'
require 'benchmark'

# Measure the time taken to execute the code
time = Benchmark.measure do
  # Initialize variables to track the least expensive city and products
  city_prices = Hash.new(0)
  product_prices = Hash.new(Float::INFINITY)

  # Open the file for reading
  CSV.foreach("../ten_million_data.csv") do |row|
    city, product, price = row
    price = price.to_f

    # Update city prices
    city_prices[city] += price

    # Update product prices
    if price < product_prices[product]
      product_prices[product] = price
    end
  end

  # Find the least expensive city
  cheapest_city = city_prices.min_by { |_, total_price| total_price }

  # Find the 5 least expensive products
  cheapest_products = product_prices.sort_by { |_, price| price }.first(5)

  # Print the results
  puts "Ville la moins chère: #{cheapest_city[0]} #{format('%.2f', cheapest_city[1])}"
  puts "5 produits les moins chers:"
  cheapest_products.each do |product, price|
    puts "#{product} #{format('%.2f', price)}"
  end
end

# Print the time taken
puts "Temps écoulé: #{time.real} secondes"
