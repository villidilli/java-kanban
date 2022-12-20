package ru.yandex.practicum.managers;

public class ManagerNotFoundException extends RuntimeException {

	public ManagerNotFoundException (String message) {
		super(message);
	}
}
