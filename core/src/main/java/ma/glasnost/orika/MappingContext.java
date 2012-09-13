/*
 * Orika - simpler, better and faster Java bean mapping
 * 
 * Copyright (C) 2011 Orika authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ma.glasnost.orika;

import java.util.HashMap;
import java.util.Map;

import ma.glasnost.orika.metadata.Type;
import ma.glasnost.orika.metadata.TypeFactory;

public class MappingContext {

	private final Map<Type<?>, Type<?>> mapping;
	private final Map<Type<?>, Map<Object, Object>> cache;

	public MappingContext() {
		mapping = new HashMap<Type<?>, Type<?>>();
		cache = new HashMap<Type<?>, Map<Object, Object>>();
	}

	@SuppressWarnings("unchecked")
	public <S, D> Type<? extends D> getConcreteClass(Type<S> sourceType,
			Type<D> destinationType) {

		final Type<?> type = mapping.get(sourceType);
		if (type != null && destinationType.isAssignableFrom(type)) {
			return (Type<? extends D>) type;
		}
		return null;
	}

	public void registerConcreteClass(Type<?> subjectClass,
			Type<?> concreteClass) {
		mapping.put(subjectClass, concreteClass);
	}

	@Deprecated
	public <S, D> void cacheMappedObject(S source, D destination) {
		cacheMappedObject(source, TypeFactory.typeOf(destination), destination);
	}

	public <S, D> void cacheMappedObject(S source, Type<D> destinationType,
			D destination) {

		Map<Object, Object> localCache = cache.get(destinationType);
		if (localCache == null) {
			localCache = new HashMap<Object, Object>();
			cache.put(destinationType, localCache);
		}
		localCache.put(source, destination);
	}

	/**
	 * @param source
	 * @param destinationType
	 * @return
	 * @deprecated use {@link #getMappedObject(Object, Type)} instead
	 */
	@Deprecated
	public <S, D> boolean isAlreadyMapped(S source, Type<D> destinationType) {
		
		Map<Object, Object> localCache = cache.get(destinationType);
		return (localCache != null && localCache.get(source) != null);
	}

	@SuppressWarnings("unchecked")
	public <D> D getMappedObject(Object source, Type<D> destinationType) {
		Map<Object, Object> localCache = cache.get(destinationType);
		return (D) (localCache == null ? null : localCache.get(source));
	}
	
}