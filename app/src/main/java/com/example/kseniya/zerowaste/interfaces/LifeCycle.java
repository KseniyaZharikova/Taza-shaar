package com.example.kseniya.zerowaste.interfaces;

public interface LifeCycle<V> {
	void bind(V view);

	void unbind();
}
