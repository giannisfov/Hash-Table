package org.hua.HashTable;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

import org.hua.HashTable.Dictionary.Entry;

public class Hashtable<K, V> implements Dictionary<K, V>{
	
	private static final int DEFAULT_SIZE = 64;
	
	private Entry<K, V> array[]; // to vasiko array tou hashtable
	
	private int size; // metavliti pou krataei to sinolo ton stoixeion
	
	private int HashArray[][]; // to array gia ton katakermatismo pou einai gemato me 0 kai 1
	
	public Hashtable(int size) {
		if (size <= 0) {
			throw new IllegalArgumentException("Size of array must be positive");
		}
		this.size = 0;
		this.array = (EntryImpl<K, V>[]) Array.newInstance(EntryImpl.class, size);
		rehash(size); // gemizei ton pinaka me 0, 1 analoga me to megethos pou theloume
	}
	
	public Hashtable() {
		this(DEFAULT_SIZE);
	}
	
	@Override
	public void put(K key, V value) {
		if (size >= array.length - 5) {
			IncreseSize(); // megalonei ton pinaka ama xreiastei
		}
		insert(key, value); // prosthetei to stoixeio
		size++;
	}

	@Override
	public V remove(K key) {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		if (size <= array.length/4) { //  mikrainei ton pinaka ama xreiastei
			decreseSize();
		}
		Entry<K, V> tmp;
		int i = Math.abs(hashFunc(key)) % array.length;
		do {
			tmp = array[i];
			if (tmp.getKey().equals(key)) {
				V tmpVal = tmp.getValue();
				array[i] = new EntryImpl(key, null); // otan afereitai ena antikeimeno ousiastika ginete null to value tou
				return tmpVal; // gia na ksexorizei apo tis kenes theseis stin anazitisi
			}
			i = (i+1) % array.length; // to i auksanete simfona me to linear probing kai i prospelasi einai kikliki
		} while (!tmp.equals(null));
		return null; // ama to antikeimeno den vrethei epistrefete null
	}

	@Override
	public V get(K key) {
		Entry<K, V> tmp;
		int i = Math.abs(hashFunc(key)) % array.length; // vriskei to index
		do {
			tmp = array[i];
			if (tmp.getKey().equals(key)) {
				return tmp.getValue();
			}
			i = (i+1) % array.length;
		} while (!tmp.equals(null)); //oso i thesi den einai null psaxnei to key
		// ama den vrethei epistrefei null
		
		return null;
	}

	@Override
	public boolean contains(K key) {
		return get(key) != null;
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public void clear() {
		int size = 0;
		this.array = (EntryImpl<K, V>[]) Array.newInstance(EntryImpl.class, size);
		// ksanaftiaxnei ton pinaka
	}
	
	private void insert(K key, V val) { // voithitiki sinartisi gia tin prosthiki stoixeiou
		int i = Math.abs(hashFunc(key)) % array.length;
		while ((!array[i].equals(null)) && (!array[i].getValue().equals(null))) {
			// ama einai keni i thesi i exei diagrafei to stoixeio pou itan prin (null to value) epilegei ekeini ti thesi
			if (array[i].getKey().equals(key)) { // ama to key iparxei idi epilegei ekeini ti thesi
				break;
			}
			i = (i + 1) % array.length; //linear probing
		}
		
		array[i] = new EntryImpl<>(key, val); // neo stoixio
	}
	
	
	private Integer hashFunc(K key) {
		return null;
	}
	
	private void IncreseSize() { // sinartisi gia na megalosei to array
		Entry<K,V>[] tempArray = array;
		array = (EntryImpl<K, V>[]) Array.newInstance(EntryImpl.class, array.length * 2);
		rehash(array.length);
		for(Entry<K, V> e : tempArray) {
			if (!e.equals(null)) {
				put(e.getKey(), e.getValue());
			}
		}
	}
	
	private void decreseSize() { // // sinartisi gia na mikrinei to array
		if (size <= DEFAULT_SIZE) { // ama to array einai idi mikrotero apo to DEFAULT_SIZE den mikrainei allo
			return;
		}
		Entry<K,V>[] tempArray = array;
		array = (EntryImpl<K, V>[]) Array.newInstance(EntryImpl.class, array.length / 2);
		rehash(array.length);
		for(Entry<K, V> e : tempArray) {
			if (!e.equals(null)) {
				put(e.getKey(), e.getValue());
			}
		}
	}

	private void rehash(int size) { // sinartisi pou gemizei ton 2d pinaka me 0 kai 1
		this.HashArray = new int[(int) Math.sqrt(size)][32];
		Random rand = new Random();
		
		for (int i = 0; i < (int) Math.sqrt(size); i++) {
			for (int j = 0; j < 32; j++) {
				this.HashArray[i][j] = rand.nextInt(2);
			}
		}
	}
	
	
	private static class EntryImpl<K, V> implements Dictionary.Entry<K, V> { // i klasi gia ta entries
		
		private K key;
		private V value;
		
		public EntryImpl(K key, V value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}
		
	}


	@Override
	public Iterator<Entry<K, V>> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

}