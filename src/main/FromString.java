package main;

public interface FromString<T> {
	public T convert(String s);
}