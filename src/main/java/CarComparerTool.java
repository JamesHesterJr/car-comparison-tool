import java.time.Year;
import java.util.*;

public class CarComparerTool {

	public List<Car> cars = Arrays.asList(
        new Car("Honda", "CRV", "Green", 2016, 23845, 80, 33),
        new Car("Ford", "Escape", "Red", 2017, 23590, 78, 32),
        new Car("Hyundai", "Sante Fe", "Grey", 2016, 24950, 80, 27),
        new Car("Mazda", "CX-5", "Red", 2017, 21795, 80, 35),
        new Car("Subaru", "Forester", "Blue", 2016, 22395, 84, 32)
    );

    public boolean someLibraryMethod() {
        return true;
    }

    public Car random() {
    	int index = new Random().nextInt(cars.size());
    	return cars.get(index);
    }

    public int fuelConsumption(Car car, int miles) {
    	return car.mpg * miles;
    }

    public int averageMPG(int year) {
    	// consider reduce function instead, but this demos algoritm
    	int sum = 0;
    	int counter = 0;
    	for(Car car : cars) {
    		if(car.year == year) {
    			sum += car.mpg;
    			counter ++;
    		}
    	}
    	return (int) Math.round(sum / counter);
    }

    public List<Car> orderByYear() {
	    return orderBy(new Comparator<Car>() {
			public int compare(Car car1, Car car2) {
	            return car2.year - car1.year;
			}
	    });
    }

    public List<Car> orderByMake() {
	    return orderBy(new Comparator<Car>() {
			public int compare(Car car1, Car car2) {
			   return car1.make.compareTo(car2.make);
			}
	    });
    }

    public List<Car> orderByModel() {
	    return orderBy(new Comparator<Car>() {
			public int compare(Car car1, Car car2) {
			   return car1.model.compareTo(car2.model);
			}
	    });
    }

    public List<Car> orderByPrice() {
	    return orderBy(new Comparator<Car>() {
			public int compare(Car car1, Car car2) {
			   return car1.price - car2.price;
			}
	    });
    }

    private List<Car> orderBy(Comparator<Car> comparator) {
    	List<Car> ordered = new ArrayList(cars);
    	Collections.sort(ordered, comparator);
	    return ordered;
    }

    public List<Car> orderByMpg() {
	    return orderBy(new Comparator<Car>() {
			public int compare(Car car1, Car car2) {
	            return car2.mpg - car1.mpg;
			}
	    });
    }

    // Valuation code:

    public Car bestValue() {

    	Map<String, Integer> weights = new HashMap<String, Integer>();
    	weights.put("year", 400);
    	weights.put("tcc", 800);
    	weights.put("mpg", 600);

    	int bestScore = 0;
    	Car best = cars.get(0);
    	for(Car car : cars) {
    		int currentScore = getValueScore(car, weights);
    		if(currentScore > bestScore) {
    			bestScore = currentScore;
    			best = car;
    		}
    	}
    	
    	return best;
    }

    protected int getValueScore(Car car, Map<String, Integer> weights) {
    	int score = 0;
    	for(Map.Entry<String, Integer> weightEntry : weights.entrySet()) {
	        String property = weightEntry.getKey();
	        int weight = weightEntry.getValue();

	    	switch(property) {
	    		case "year":
	    			score += getYearScore(weight, car.year);
	    			break;
	    		case "tcc":
	    			score += getTCCScore(weight, car.tccRating);
	    			break;
	    		case "mpg":
	    			score += getMPGScore(weight, car.mpg);
	    			break;
	    	}
	    }

	    // compare to price for value score
	    return score / car.price;
    }

    protected int getYearScore(int weight, int year) {
    	int score = 0;
		int cutoff = 20;
		int age = Year.now().getValue() - year;
		if(age < cutoff) {
			score = valueFormula(weight, cutoff, cutoff - age);
		}
		return score;
    }

    protected int getTCCScore(int weight, int tcc) {
		int scale = 100;
		return valueFormula(weight, scale, tcc);
    }

    protected int getMPGScore(int weight, int mpg) {

    	int bestMpg = orderByMpg().get(0).mpg;
    	int worstMpg = orderByMpg().get(cars.size() - 1).mpg;

    	int scale = bestMpg - worstMpg;
		return valueFormula(weight, scale, mpg);
    }

    protected int valueFormula(int weight, int scale, int value) {
    	return (int) Math.round(weight / scale * value);
    }
}

class Car {	
	public String make;
	public String model;
	public String color;
	public int year;
	public int price;
	public int tccRating;
	public int mpg;

	public Car(String make, String model, String color, int year, int price, int tccRating, int mpg) {
		this.make = make;
		this.model = model;
		this.color = color;
		this.year = year;
		this.price = price;
		this.tccRating = tccRating;
		this.mpg = mpg;
	}
}