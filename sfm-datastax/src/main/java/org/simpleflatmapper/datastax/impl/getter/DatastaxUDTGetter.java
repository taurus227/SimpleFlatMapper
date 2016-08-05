package org.simpleflatmapper.datastax.impl.getter;

import com.datastax.driver.core.GettableByIndexData;
import com.datastax.driver.core.UserType;
import org.simpleflatmapper.datastax.DatastaxColumnKey;
import org.simpleflatmapper.datastax.DatastaxMapperBuilder;
import org.simpleflatmapper.datastax.DatastaxMapperFactory;
import org.simpleflatmapper.datastax.impl.DatastaxMappingContextFactoryBuilder;
import org.simpleflatmapper.datastax.impl.RowGetterFactory;
import org.simpleflatmapper.core.map.Mapper;
import org.simpleflatmapper.core.map.MapperConfig;
import org.simpleflatmapper.core.map.column.FieldMapperColumnDefinition;
import org.simpleflatmapper.core.map.mapper.FieldMapperMapperBuilder;
import org.simpleflatmapper.core.map.mapper.MapperSourceImpl;
import org.simpleflatmapper.core.reflect.Getter;
import org.simpleflatmapper.core.reflect.meta.ClassMeta;

import java.lang.reflect.Type;
import java.util.Iterator;

public class DatastaxUDTGetter<T> implements Getter<GettableByIndexData, T> {

    private final Mapper<GettableByIndexData, T> mapper;
    private final int index;

    public DatastaxUDTGetter(Mapper<GettableByIndexData, T> mapper, int index) {
        this.mapper = mapper;
        this.index = index;
    }

    @Override
    public T get(GettableByIndexData target) throws Exception {
        return mapper.map(target.getUDTValue(index));
    }

    @SuppressWarnings("unchecked")
    public static <P> Getter<GettableByIndexData, P> newInstance(DatastaxMapperFactory factory, Type target,  UserType tt, int index) {
        Mapper<GettableByIndexData, P> mapper = newUDTMapper(target, tt, factory);
        return new DatastaxUDTGetter<P>(mapper, index);
    }

    public static <P> Mapper<GettableByIndexData, P> newUDTMapper(Type target, UserType tt, DatastaxMapperFactory factory) {
        FieldMapperMapperBuilder<GettableByIndexData, P, DatastaxColumnKey> builder = newFieldMapperBuilder(factory, target);

        Iterator<UserType.Field> iterator = tt.iterator();
        int i = 0;
        while(iterator.hasNext()) {
            UserType.Field f = iterator.next();
            FieldMapperColumnDefinition<DatastaxColumnKey> identity = FieldMapperColumnDefinition.identity();
            builder.addMapping(new DatastaxColumnKey(f.getName(), i, f.getType()), identity);
            i ++;
        }

        return builder.mapper();
    }

    public static <P> FieldMapperMapperBuilder<GettableByIndexData, P, DatastaxColumnKey> newFieldMapperBuilder(DatastaxMapperFactory factory, Type target) {
        MapperConfig<DatastaxColumnKey, FieldMapperColumnDefinition<DatastaxColumnKey>> config = factory.mapperConfig();
        MapperSourceImpl<GettableByIndexData, DatastaxColumnKey> mapperSource = new MapperSourceImpl<GettableByIndexData, DatastaxColumnKey>(GettableByIndexData.class, new RowGetterFactory(factory));
        ClassMeta<P> classMeta = factory.getClassMeta(target);
        return new FieldMapperMapperBuilder<GettableByIndexData, P, DatastaxColumnKey>(
                mapperSource,
                classMeta,
                config,
                new DatastaxMappingContextFactoryBuilder(),
                DatastaxMapperBuilder.KEY_FACTORY);
    }
}