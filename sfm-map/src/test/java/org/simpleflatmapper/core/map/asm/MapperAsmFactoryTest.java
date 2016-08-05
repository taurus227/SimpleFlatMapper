package org.simpleflatmapper.core.map.asm;

import org.junit.Test;
import org.simpleflatmapper.core.map.FieldMapper;
import org.simpleflatmapper.core.map.Mapper;
import org.simpleflatmapper.core.map.MappingContext;
import org.simpleflatmapper.core.map.MappingException;
import org.simpleflatmapper.core.map.SampleFieldKey;
import org.simpleflatmapper.core.reflect.Instantiator;
import org.simpleflatmapper.core.reflect.asm.AsmFactory;
import org.simpleflatmapper.test.beans.DbObject;

import java.io.IOException;
import static org.junit.Assert.assertTrue;

public class MapperAsmFactoryTest {

	static MapperAsmFactory asmFactory = new MapperAsmFactory(new AsmFactory(Thread.currentThread().getContextClassLoader()));
	

	@SuppressWarnings("unchecked")
	@Test
	public void testAsmJdbcMapperFailedInstantiator() throws Exception {
		Mapper<Object, DbObject> jdbcMapper =
				asmFactory.createMapper(new SampleFieldKey[0],
				(FieldMapper<Object, DbObject>[]) new FieldMapper[]{},
				(FieldMapper<Object, DbObject>[]) new FieldMapper[]{},
				new Instantiator<Object, DbObject>() {
					@Override
					public DbObject newInstance(Object s) throws Exception {
						throw new IOException("Error");
					}
				},Object.class,
				DbObject.class);
		
		try {
			jdbcMapper.map(null);
		} catch(Exception e) {
            assertTrue(e instanceof IOException);
			// ok
		} 
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void testAsmJdbcMapperFailedGetter() throws Exception {
		Mapper<Object, DbObject> jdbcMapper = asmFactory.createMapper(new SampleFieldKey[0],
				(FieldMapper<Object, DbObject>[]) new FieldMapper[]{
						new FieldMapper<Object, DbObject>() {
							@Override
							public void mapTo(Object source, DbObject target, MappingContext<? super Object> mappingContext)
									throws MappingException {
								throw new MappingException("Expected ");
							}
						}
				},
				(FieldMapper<Object, DbObject>[]) new FieldMapper[]{},
				new Instantiator<Object, DbObject>() {
					@Override
					public DbObject newInstance(Object s) throws Exception {
						return new DbObject();
					}
				}, Object.class,
				DbObject.class);
		
		try {
			jdbcMapper.map(null);
		} catch(MappingException e) {
			// ok
		} 
	}
}