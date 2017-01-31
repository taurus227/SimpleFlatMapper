package org.simpleflatmapper.reflect.impl;

import org.simpleflatmapper.reflect.BiInstantiator;
import org.simpleflatmapper.reflect.Getter;
import org.simpleflatmapper.reflect.Instantiator;
import org.simpleflatmapper.reflect.InstantiatorDefinition;
import org.simpleflatmapper.reflect.Parameter;
import org.simpleflatmapper.reflect.instantiator.ExecutableInstantiatorDefinition;
import org.simpleflatmapper.util.BiFactory;
import org.simpleflatmapper.util.ErrorHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public final class InjectConstructorBiInstantiator<S1, S2, T> implements BiInstantiator<S1, S2, T> {

	private final Constructor<? extends T> constructor;
	private final BiArgumentBuilder<S1, S2> argBuilder;
	private final InstantiatorDefinition instantiatorDefinition;

	@SuppressWarnings("unchecked")
	public InjectConstructorBiInstantiator(ExecutableInstantiatorDefinition instantiatorDefinition, Map<Parameter, BiFactory<? super S1, ? super S2, ?>> injections) {
		this.argBuilder = new BiArgumentBuilder<S1, S2>(instantiatorDefinition, injections);
		this.constructor = (Constructor<? extends T>) instantiatorDefinition.getExecutable();
		this.instantiatorDefinition = instantiatorDefinition;
	}

	@Override
	public T newInstance(S1 s1, S2 s2) throws Exception {
		try {
			return constructor.newInstance(argBuilder.build(s1, s2));
		} catch(InvocationTargetException e) {
			return ErrorHelper.rethrow(e.getCause());
		}
	}

    @Override
    public String toString() {
        return "InjectConstructorBiInstantiator{" +
                "instantiatorDefinition=" + instantiatorDefinition +
                '}';
    }
}