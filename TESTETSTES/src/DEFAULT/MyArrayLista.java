package Labb2;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

@SuppressWarnings("serial")
public class MyArrayLista<E> implements Serializable, Cloneable, Iterable<E>, Collection<E>, List<E>, RandomAccess {
// ---------------------------------------------------------------
	private E[] array;
	private int antalelement = 0;

	public static void main(String[] args) {
//		MyArrayLista<String> strlist = new MyArrayLista<String>(6);
	}

	// ---------------------------------------------------------------
	public MyArrayLista(int initialCapacity) { // Constructs an empty list with the specified initial capacity.
		if (initialCapacity < 0) {
			throw new IllegalArgumentException();
		}
		this.array = (E[]) new Object[initialCapacity];
	}

	public MyArrayLista() { // Constructs an empty list with the initial capacity of 10.
		this.array = (E[]) new Object[10];
	}

	// Hjälp metoder
	private String arrysizes() {
		return (String) this.array[1];
	}

	private int arrysize() {
		return this.array.length;
	}

	// -- 1 --

	@Override
	public int size() { // Returns the number of elements in this list.
		return antalelement;
	}

	@Override
	public boolean isEmpty() { // Returns true if this list contains no elements.
		return antalelement == 0;
	}

	@Override
	public void clear() { // Removes all of the elements from this list.¨
		antalelement = 0;
	}

	// -- 2 --

	public void ensureCapacity(int minCapacity) { // Increases the capacity of this ArrayList instance, if necessary, to
													// ensure that it can hold at least the number of elements specified
													// by the minimum capacity argument.
		if (minCapacity > this.array.length) { // minCapacity ska vara större än längden av array.length
			E[] copyarray = (E[]) new Object[this.array.length];
			for (int i = 0; i < this.array.length; i++) {
				copyarray[i] = this.array[i];
			}
			this.array = (E[]) new Object[minCapacity]; // ny array med större kapacitet
			for (int i = 0; i < copyarray.length; i++) { // Föra över element från gamla array
				this.array[i] = copyarray[i];
			}
		} else {
		}
	}

	public void trimToSize() { // Trims the capacity of this ArrayList instance to be the list's current size.
		E[] copyarray = (E[]) new Object[antalelement];
		for (int i = 0; i < antalelement; i++) {
			copyarray[i] = this.array[i];
		}
		this.array = (E[]) new Object[antalelement];
		for (int i = 0; i < antalelement; i++) {
			this.array[i] = copyarray[i];
		}
	}

	// -- 3 --

	@Override
	public void add(int index, E element) { // Inserts the specified element at the specified position in this list.
		ensureCapacity(this.array.length + 1); // Blir lite oeffektivt.
		if (this.array[index] == null) { // Lätt, om det är inget där, lägg till element
			this.array[index] = element;
			antalelement += 1;
		} else {
			for (int i = antalelement; i > index; i--) { // Om det är något där, flytta fram elementen så att den nya
															// elementen får plats
				this.array[i] = this.array[i - 1];
			}
			this.array[index] = element;
			antalelement += 1;
		}
	}

	@Override
	public boolean add(E element) {
		add(antalelement, element);
		return true;
	}

	// -- 4 --

	@Override
	public E get(int index) {
		return this.array[index];
	}

	@Override
	public E set(int index, E element) {
		E previousElement = this.array[index]; // Spara gamla elementen
		this.array[index] = element; // Byt till det nya elementen
		return previousElement; // Skicka tillbaka den gamla elementen
	}

	// -- 5 --

	@Override
	public E remove(int index) {
		E deletedElement = this.array[index];
		for (int i = index; i < this.array.length - 1; i++) { // Om det är något där, flytta fram elementen så att den
																// nya elementen får plats
			this.array[i] = this.array[i + 1];
		}
		this.array[this.array.length - 1] = null; // Loppen tar inte bort den sista elementen, det gör vi här
		if (deletedElement != null) { // Antar att null räknas inte som en element utan är där för att kunna beskriva
										// storleken på arrayn
			antalelement -= 1;
		}
		return deletedElement;
	}

	protected void removeRange(int fromIndex, int toIndex) {
		for (int i = toIndex - 1; i >= fromIndex; i--) {
			remove(i);
		}
	}

	// -- 6 --

	@Override
	public int indexOf(Object element) {
		for (int i = 0; i < this.array.length; i++) {
			if (element.equals(get(i))) { // Så fort den hittar den sökta elementen så retunerar den dess index
				return i;
			}
		}
		return -1;
	}

	@Override
	public boolean remove(Object element) { // Den skiftar listan till vänster varje gång vi raderar, vet inte om det
											// ska vara så
		if (indexOf(element) > -1) {
			remove(indexOf(element));
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean contains(Object element) {
		if (indexOf(element) > -1) {
			return true;
		} else {
			return false;
		}
	}

	// -- 7 --

	@Override
	public Object clone() {
		MyArrayLista<E> copyarray = new MyArrayLista<E>();
		copyarray.antalelement = antalelement;
		copyarray.array = this.array;
		return copyarray;
	}

	@Override
	public Object[] toArray() {
		int sistaElementIndex = 0;
		for (int i = this.array.length - 1; i > 0; i--) { // Bestämma sista index
			if (this.array[i] == null) {
				continue;
			} else {
				sistaElementIndex = i;
				break;
			}
		}
		E[] copyarray = (E[]) new Object[sistaElementIndex + 1];
		for (int i = 0; i <= sistaElementIndex; i++) { // Kopierar element upp till sista index
			copyarray[i] = this.array[i];
		}
		return copyarray;
	}

	// --- Rör ej nedanstående kod -----------------------------------
	public MyArrayLista(Collection<? extends E> c) {
		throw new UnsupportedOperationException();
	}

	private class InternalIterator implements Iterator {
		int current = 0;

		@Override
		public boolean hasNext() {
			return current < size();
		}

		@Override
		public Object next() {
			return get(current++);
		}

	}

	@Override
	public Iterator<E> iterator() {
		return new InternalIterator();
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int lastIndexOf(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<E> listIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void forEach(Consumer<? super E> action) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Spliterator<E> spliterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeIf(Predicate<? super E> filter) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void replaceAll(UnaryOperator<E> operator) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void sort(Comparator<? super E> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}
}
