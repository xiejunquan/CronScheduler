package com.scheduler.conf;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 方法参数可选的参数枚举
 * @author 谢俊权
 * @create 2016/9/13 10:09
 */
public enum ParamType {

    BYTE("byte", Byte.class) {
        @Override
        public Byte convert(String value) {
            return Byte.valueOf(value);
        }
    },

    SHORT("short", Short.class) {
        @Override
        public Short convert(String value) {
            return Short.valueOf(value);
        }
    },

    INT("int", Integer.class) {
        @Override
        public Integer convert(String value) {
            return Integer.valueOf(value);
        }
    },

    LONG("long", Long.class) {
        @Override
        public Long convert(String value) {
            return Long.valueOf(value);
        }
    },

    BOOLEAN("boolean", Boolean.class) {
        @Override
        public Boolean convert(String value) {
            return Boolean.valueOf(value);
        }
    },

    FLOAT("float", Float.class) {
        @Override
        public Float convert(String value) {
            return Float.valueOf(value);
        }
    },

    DOUBLE("double", Double.class) {
        @Override
        public Double convert(String value) {
            return Double.valueOf(value);
        }
    },

    CHAR("char", Character.class) {
        @Override
        public Character convert(String value) {
            return value.charAt(0);
        }
    },

    STRING("string", String.class) {
        @Override
        public String convert(String value) {
            return value;
        }
    };

    private static final Map<String, ParamType> lookup = new HashMap<>();
    static {
        for(ParamType type : EnumSet.allOf(ParamType.class)){
            lookup.put(type.typeName, type);
        }
    }



    private String typeName;
    private Class type;

    ParamType(String typeName, Class type) {
        this.typeName = typeName;
        this.type = type;
    }

    public Class getType() {
        return type;
    }

    public static ParamType lookup(String type){
        return lookup.get(type);
    }

    public abstract <T> T convert(String value);



}
