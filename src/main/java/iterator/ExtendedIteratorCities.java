package iterator;

import java.util.List;

public class ExtendedIteratorCities implements ExtendedIterator<String>{
	List<String> cities;
	int posicion;
	
	public ExtendedIteratorCities(List<String> c) {
		this.cities = c;
		this.posicion = 0;
	}
	

	@Override
	public boolean hasNext() {
		return posicion < cities.size();
	}

	@Override
	public Object next() {
		String city = cities.get(posicion);
		posicion++;
		return city;
	}

	@Override
	public Object previous() {
		posicion--;
		String city = cities.get(posicion);
		return city;
	}

	@Override
	public boolean hasPrevious() {
		return posicion > 0;
	}

	@Override
	public void goFirst() {
		posicion = 0;
	}

	@Override
	public void goLast() {
		posicion = cities.size();
		
	}
	

}
