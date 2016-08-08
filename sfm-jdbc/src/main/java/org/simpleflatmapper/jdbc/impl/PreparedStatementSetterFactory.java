package org.simpleflatmapper.jdbc.impl;

import org.simpleflatmapper.converter.ComposedConverter;
import org.simpleflatmapper.converter.Converter;
import org.simpleflatmapper.converter.ConverterService;
import org.simpleflatmapper.jdbc.JdbcColumnKey;

import org.simpleflatmapper.jdbc.converter.UtilDateToDateConverter;
import org.simpleflatmapper.jdbc.converter.UtilDateToTimeConverter;
import org.simpleflatmapper.jdbc.converter.UtilDateToTimestampConverter;
import org.simpleflatmapper.map.mapper.ColumnDefinition;
import org.simpleflatmapper.map.mapper.PropertyMapping;
import org.simpleflatmapper.reflect.IndexedSetter;
import org.simpleflatmapper.reflect.IndexedSetterFactory;
import org.simpleflatmapper.reflect.Setter;
import org.simpleflatmapper.reflect.SetterFactory;
import org.simpleflatmapper.util.TypeHelper;
import org.simpleflatmapper.jdbc.impl.setter.ArrayPreparedStatementIndexSetter;
import org.simpleflatmapper.jdbc.impl.setter.BigDecimalPreparedStatementIndexSetter;
import org.simpleflatmapper.jdbc.impl.setter.BlobPreparedStatementIndexSetter;
import org.simpleflatmapper.jdbc.impl.setter.BooleanPreparedStatementIndexSetter;
import org.simpleflatmapper.jdbc.impl.setter.BooleanPreparedStatementSetter;
import org.simpleflatmapper.jdbc.impl.setter.BytePreparedStatementIndexSetter;
import org.simpleflatmapper.jdbc.impl.setter.BytePreparedStatementSetter;
import org.simpleflatmapper.jdbc.impl.setter.BytesPreparedStatementIndexSetter;
import org.simpleflatmapper.jdbc.impl.setter.CharacterPreparedStatementIndexSetter;
import org.simpleflatmapper.jdbc.impl.setter.CharacterPreparedStatementSetter;
import org.simpleflatmapper.jdbc.impl.setter.ClobPreparedStatementIndexSetter;
import org.simpleflatmapper.jdbc.impl.setter.ConvertDelegateIndexSetter;
import org.simpleflatmapper.jdbc.impl.setter.DatePreparedStatementIndexSetter;
import org.simpleflatmapper.jdbc.impl.setter.DoublePreparedStatementIndexSetter;
import org.simpleflatmapper.jdbc.impl.setter.DoublePreparedStatementSetter;
import org.simpleflatmapper.jdbc.impl.setter.FloatPreparedStatementIndexSetter;
import org.simpleflatmapper.jdbc.impl.setter.FloatPreparedStatementSetter;
import org.simpleflatmapper.jdbc.impl.setter.InputStreamPreparedStatementIndexSetter;
import org.simpleflatmapper.jdbc.impl.setter.IntegerPreparedStatementIndexSetter;
import org.simpleflatmapper.jdbc.impl.setter.IntegerPreparedStatementSetter;
import org.simpleflatmapper.jdbc.impl.setter.LongPreparedStatementIndexSetter;
import org.simpleflatmapper.jdbc.impl.setter.LongPreparedStatementSetter;
import org.simpleflatmapper.jdbc.impl.setter.NClobPreparedStatementIndexSetter;
import org.simpleflatmapper.jdbc.impl.setter.ObjectPreparedStatementIndexSetter;
import org.simpleflatmapper.jdbc.impl.setter.OrdinalEnumPreparedStatementIndexSetter;
import org.simpleflatmapper.jdbc.impl.setter.PreparedStatementIndexSetter;
import org.simpleflatmapper.jdbc.impl.setter.PreparedStatementSetterImpl;
import org.simpleflatmapper.jdbc.impl.setter.ReaderPreparedStatementIndexSetter;
import org.simpleflatmapper.jdbc.impl.setter.RefPreparedStatementIndexSetter;
import org.simpleflatmapper.jdbc.impl.setter.RowIdPreparedStatementIndexSetter;
import org.simpleflatmapper.jdbc.impl.setter.SQLXMLPreparedStatementIndexSetter;
import org.simpleflatmapper.jdbc.impl.setter.ShortPreparedStatementIndexSetter;
import org.simpleflatmapper.jdbc.impl.setter.ShortPreparedStatementSetter;
import org.simpleflatmapper.jdbc.impl.setter.StringEnumPreparedStatementIndexSetter;
import org.simpleflatmapper.jdbc.impl.setter.StringPreparedStatementIndexSetter;
import org.simpleflatmapper.jdbc.impl.setter.TimePreparedStatementIndexSetter;
import org.simpleflatmapper.jdbc.impl.setter.TimestampPreparedStatementIndexSetter;
import org.simpleflatmapper.jdbc.impl.setter.URLPreparedStatementIndexSetter;
import org.simpleflatmapper.jdbc.impl.setter.UUIDBinaryPreparedStatementIndexSetter;
import org.simpleflatmapper.jdbc.impl.setter.UUIDStringPreparedStatementIndexSetter;

import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PreparedStatementSetterFactory
        implements
        SetterFactory<PreparedStatement, PropertyMapping<?, ?, JdbcColumnKey, ? extends ColumnDefinition<JdbcColumnKey, ?>>>,
        IndexedSetterFactory<PreparedStatement, PropertyMapping<?, ?, JdbcColumnKey, ? extends ColumnDefinition<JdbcColumnKey, ?>>>
        {
    private final Map<Class<?>, Factory> factoryPerClass =
            new HashMap<Class<?>, Factory>();

    {
        factoryPerClass.put(boolean.class,
                new Factory() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public <P> PreparedStatementIndexSetter<P> getIndexedSetter(JdbcColumnKey key) {
                        return (PreparedStatementIndexSetter<P>) new BooleanPreparedStatementIndexSetter();
                    }
                });
        factoryPerClass.put(Boolean.class, factoryPerClass.get(boolean.class));

        factoryPerClass.put(byte.class,
                new Factory() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public <P> PreparedStatementIndexSetter<P> getIndexedSetter(JdbcColumnKey key) {
                        return (PreparedStatementIndexSetter<P>) new BytePreparedStatementIndexSetter();
                    }
                });
        factoryPerClass.put(Byte.class, factoryPerClass.get(byte.class));

        factoryPerClass.put(char.class,
                new Factory() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public <P> PreparedStatementIndexSetter<P> getIndexedSetter(JdbcColumnKey key) {
                        return (PreparedStatementIndexSetter<P>) new CharacterPreparedStatementIndexSetter();
                    }
                });
        factoryPerClass.put(Character.class, factoryPerClass.get(char.class));

        factoryPerClass.put(short.class,
                new Factory() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public <P> PreparedStatementIndexSetter<P> getIndexedSetter(JdbcColumnKey key) {
                        return (PreparedStatementIndexSetter<P>) new ShortPreparedStatementIndexSetter();
                    }
                });
        factoryPerClass.put(Short.class, factoryPerClass.get(short.class));

        factoryPerClass.put(int.class,
                new Factory() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public <P> PreparedStatementIndexSetter<P> getIndexedSetter(JdbcColumnKey key) {
                        return (PreparedStatementIndexSetter<P>) new IntegerPreparedStatementIndexSetter();
                    }
                });
        factoryPerClass.put(Integer.class, factoryPerClass.get(int.class));

        factoryPerClass.put(long.class,
                new Factory() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public <P> PreparedStatementIndexSetter<P> getIndexedSetter(JdbcColumnKey key) {
                        return (PreparedStatementIndexSetter<P>) new LongPreparedStatementIndexSetter();
                    }
                });
        factoryPerClass.put(Long.class, factoryPerClass.get(long.class));

        factoryPerClass.put(float.class,
                new Factory() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public <P> PreparedStatementIndexSetter<P> getIndexedSetter(JdbcColumnKey key) {
                        return (PreparedStatementIndexSetter<P>) new FloatPreparedStatementIndexSetter();
                    }
                });
        factoryPerClass.put(Float.class, factoryPerClass.get(float.class));

        factoryPerClass.put(double.class,
                new Factory() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public <P> PreparedStatementIndexSetter<P> getIndexedSetter(JdbcColumnKey key) {
                        return (PreparedStatementIndexSetter<P>) new DoublePreparedStatementIndexSetter();
                    }
                });
        factoryPerClass.put(Double.class, factoryPerClass.get(double.class));

        factoryPerClass.put(String.class,
                new Factory() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public <P> PreparedStatementIndexSetter<P> getIndexedSetter(JdbcColumnKey key) {
                        return (PreparedStatementIndexSetter<P>) new StringPreparedStatementIndexSetter();
                    }
                });
        factoryPerClass.put(Timestamp.class,
                new Factory() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public <P> PreparedStatementIndexSetter<P> getIndexedSetter(JdbcColumnKey key) {
                        return (PreparedStatementIndexSetter<P>) new TimestampPreparedStatementIndexSetter();
                    }
                });
        factoryPerClass.put(java.sql.Date.class,
                new Factory() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public <P> PreparedStatementIndexSetter<P> getIndexedSetter(JdbcColumnKey key) {
                        return (PreparedStatementIndexSetter<P>) new DatePreparedStatementIndexSetter();
                    }
                });
        factoryPerClass.put(Time.class,
                new Factory() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public <P> PreparedStatementIndexSetter<P> getIndexedSetter(JdbcColumnKey key) {
                        return (PreparedStatementIndexSetter<P>) new TimePreparedStatementIndexSetter();
                    }
                });
        factoryPerClass.put(URL.class,
                new Factory() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public <P> PreparedStatementIndexSetter<P> getIndexedSetter(JdbcColumnKey key) {
                        return (PreparedStatementIndexSetter<P>) new URLPreparedStatementIndexSetter();
                    }
                });
        factoryPerClass.put(Ref.class,
                new Factory() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public <P> PreparedStatementIndexSetter<P> getIndexedSetter(JdbcColumnKey key) {
                        return (PreparedStatementIndexSetter<P>) new RefPreparedStatementIndexSetter();
                    }
                });
        factoryPerClass.put(BigDecimal.class,
                new Factory() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public <P> PreparedStatementIndexSetter<P> getIndexedSetter(JdbcColumnKey key) {
                        return (PreparedStatementIndexSetter<P>) new BigDecimalPreparedStatementIndexSetter();
                    }
                });
        factoryPerClass.put(Array.class,
                new Factory() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public <P> PreparedStatementIndexSetter<P> getIndexedSetter(JdbcColumnKey key) {
                        return (PreparedStatementIndexSetter<P>) new ArrayPreparedStatementIndexSetter();
                    }
                });
        factoryPerClass.put(byte[].class,
                new Factory() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public <P> PreparedStatementIndexSetter<P> getIndexedSetter(JdbcColumnKey key) {
                        return (PreparedStatementIndexSetter<P>) new BytesPreparedStatementIndexSetter();
                    }
                });
        factoryPerClass.put(NClob.class,
                new Factory() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public <P> PreparedStatementIndexSetter<P> getIndexedSetter(JdbcColumnKey key) {
                        return (PreparedStatementIndexSetter<P>) new NClobPreparedStatementIndexSetter();
                    }
                });
        factoryPerClass.put(RowId.class,
                new Factory() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public <P> PreparedStatementIndexSetter<P> getIndexedSetter(JdbcColumnKey key) {
                        return (PreparedStatementIndexSetter<P>) new RowIdPreparedStatementIndexSetter();
                    }
                });
        factoryPerClass.put(Blob.class,
                new Factory() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public <P> PreparedStatementIndexSetter<P> getIndexedSetter(JdbcColumnKey key) {
                        return (PreparedStatementIndexSetter<P>) new BlobPreparedStatementIndexSetter();
                    }
                });
        factoryPerClass.put(Clob.class,
                new Factory() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public <P> PreparedStatementIndexSetter<P> getIndexedSetter(JdbcColumnKey key) {
                        return (PreparedStatementIndexSetter<P>) new ClobPreparedStatementIndexSetter();
                    }
                });
        factoryPerClass.put(InputStream.class,
                new Factory() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public <P> PreparedStatementIndexSetter<P> getIndexedSetter(JdbcColumnKey key) {
                        return (PreparedStatementIndexSetter<P>) new InputStreamPreparedStatementIndexSetter();
                    }
                });
        factoryPerClass.put(Reader.class,
                new Factory() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public <P> PreparedStatementIndexSetter<P> getIndexedSetter(JdbcColumnKey key) {
                        return (PreparedStatementIndexSetter<P>) new ReaderPreparedStatementIndexSetter();
                    }
                });
        factoryPerClass.put(SQLXML.class,
                new Factory() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public <P> PreparedStatementIndexSetter<P> getIndexedSetter(JdbcColumnKey key) {
                        return (PreparedStatementIndexSetter<P>) new SQLXMLPreparedStatementIndexSetter();
                    }
                });

        factoryPerClass.put(UUID.class,
                new Factory() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public <P> PreparedStatementIndexSetter<P> getIndexedSetter(JdbcColumnKey key) {
                        switch (key.getSqlType()) {
                            case Types.BINARY:
                            case Types.VARBINARY:
                            case Types.LONGVARBINARY:
                                return (PreparedStatementIndexSetter<P>) new UUIDBinaryPreparedStatementIndexSetter();
                        }
                        return (PreparedStatementIndexSetter<P>) new UUIDStringPreparedStatementIndexSetter();
                    }
                });
    }

    @SuppressWarnings("unchecked")
    @Override
    public <P> Setter<PreparedStatement, P> getSetter(PropertyMapping<?, ?, JdbcColumnKey, ? extends ColumnDefinition<JdbcColumnKey, ?>> pm) {
        int columnIndex = pm.getColumnKey().getIndex();

        Type type = pm.getPropertyMeta().getPropertyType();

        Class<?> clazz = TypeHelper.toBoxedClass(type);

        if (Boolean.class.equals(clazz)) {
            return (Setter<PreparedStatement, P>) new BooleanPreparedStatementSetter(columnIndex);
        } else if (Byte.class.equals(clazz)) {
            return (Setter<PreparedStatement, P>) new BytePreparedStatementSetter(columnIndex);
        } else if (Character.class.equals(clazz)) {
            return (Setter<PreparedStatement, P>) new CharacterPreparedStatementSetter(columnIndex);
        } else if (Short.class.equals(clazz)) {
            return (Setter<PreparedStatement, P>) new ShortPreparedStatementSetter(columnIndex);
        } else if (Integer.class.equals(clazz)) {
            return (Setter<PreparedStatement, P>) new IntegerPreparedStatementSetter(columnIndex);
        } else if (Long.class.equals(clazz)) {
            return (Setter<PreparedStatement, P>) new LongPreparedStatementSetter(columnIndex);
        } else if (Double.class.equals(clazz)) {
            return (Setter<PreparedStatement, P>) new DoublePreparedStatementSetter(columnIndex);
        } else if (Float.class.equals(clazz)) {
            return (Setter<PreparedStatement, P>) new FloatPreparedStatementSetter(columnIndex);
        }

        IndexedSetter<PreparedStatement, P> setter = getIndexedSetter(pm);

        if (setter != null) {
            return new PreparedStatementSetterImpl<P>(columnIndex, setter);
        } else return null;
    }


    @Override
    public  <T> IndexedSetter<PreparedStatement, T> getIndexedSetter(PropertyMapping<?, ?, JdbcColumnKey, ? extends ColumnDefinition<JdbcColumnKey, ?>> arg) {
        Type propertyType = arg.getPropertyMeta().getPropertyType();
        IndexedSetter<PreparedStatement, T> setter = getIndexedSetter(propertyType, arg);

        if (setter == null) {
            Class<?> iclass = JDBCTypeHelper.toJavaType(arg.getColumnKey().getSqlType(), propertyType);
            setter = getSetterWithConvertion(TypeHelper.<T>toClass(propertyType), iclass, arg);
        }

        return setter;
    }


    private <P, I> IndexedSetter<PreparedStatement, P> getSetterWithConvertion(Class<P> pclazz, Class<I> iclass, PropertyMapping<?, ?, JdbcColumnKey, ? extends ColumnDefinition<JdbcColumnKey, ?>> pm) {
        Converter<? super P, ? extends I> converter = ConverterService.getInstance().findConverter(pclazz, iclass);

        if (converter == null) {
            if (iclass.equals(Timestamp.class)) {
                Converter<? super P, ? extends java.util.Date> dateConverter = ConverterService.getInstance().findConverter(pclazz, java.util.Date.class, pm.getColumnDefinition().properties());
                if (dateConverter != null) {
                    converter = new ComposedConverter<P, java.util.Date,  I>(dateConverter, (Converter<? super java.util.Date, ? extends I>) new UtilDateToTimestampConverter());
                }
            }
            if (iclass.equals(Time.class)) {
                Converter<? super P, ? extends java.util.Date> dateConverter = ConverterService.getInstance().findConverter(pclazz, java.util.Date.class, pm.getColumnDefinition().properties());
                if (dateConverter != null) {
                    converter = new ComposedConverter<P, java.util.Date,  I>(dateConverter, (Converter<? super java.util.Date, ? extends I>) new UtilDateToTimeConverter());
                }
            }
            if (iclass.equals(Date.class)) {
                Converter<? super P, ? extends java.util.Date> dateConverter = ConverterService.getInstance().findConverter(pclazz, java.util.Date.class, pm.getColumnDefinition().properties());
                if (dateConverter != null) {
                    converter = new ComposedConverter<P, java.util.Date,  I>(dateConverter, (Converter<? super java.util.Date, ? extends I>) new UtilDateToDateConverter());
                }
            }

        }
        if (converter != null) {
            IndexedSetter<PreparedStatement, I> indexedSetter = getIndexedSetter(iclass, pm);
            if (indexedSetter != null) {
                return new ConvertDelegateIndexSetter<P, I>(indexedSetter, converter);
            }
        }


        return null;
    }

    protected  <T> IndexedSetter<PreparedStatement, T> getIndexedSetter(Type propertyType, PropertyMapping<?, ?, JdbcColumnKey, ? extends ColumnDefinition<JdbcColumnKey, ?>> arg) {
        IndexedSetter<PreparedStatement, T> setter = null;

        if (TypeHelper.isEnum(propertyType)) {
            switch (arg.getColumnKey().getSqlType()) {
                case Types.NUMERIC:
                case Types.BIGINT:
                case Types.INTEGER:
                case Types.DECIMAL:
                case Types.DOUBLE:
                case Types.FLOAT:
                case Types.SMALLINT:
                case Types.REAL:
                case Types.TINYINT:
                   setter = (IndexedSetter<PreparedStatement, T>) new OrdinalEnumPreparedStatementIndexSetter();
                    break;
                default:
                    setter = (IndexedSetter<PreparedStatement, T>) new StringEnumPreparedStatementIndexSetter();
            }
        }

        if (setter == null) {
            Factory setterFactory = this.factoryPerClass.get(TypeHelper.toClass(propertyType));
            if (setterFactory != null) {
                setter = setterFactory.<T>getIndexedSetter(arg.getColumnKey());
            }
        }

        if (setter == null && TypeHelper.isAssignable(SQLData.class, propertyType)) {
            setter = (IndexedSetter<PreparedStatement, T>) new ObjectPreparedStatementIndexSetter();
        }

        return setter;
    }

    public interface Factory
            extends IndexedSetterFactory<PreparedStatement, JdbcColumnKey> {
    }

}
