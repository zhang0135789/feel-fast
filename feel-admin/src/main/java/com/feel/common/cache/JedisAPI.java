package com.feel.common.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feel.common.utils.SysPropertyUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @Description : Redis操作接口，简单操作可以通过调用提供的快速接口实现，复杂的操作自己获取jedis实例，
 * 使用过程中出现异常应调用returnResource方法销毁异常的实例
 * ，使用完成ying调用returnResource方法归还实例，具体使用方法参考快速接口
 * @Author :
 * @Creation Date : 2013-6-25 上午10:39:09
 */
public class JedisAPI {
    private static JedisPool pool = null;
    private static ObjectMapper om;

    static {
        om = new ObjectMapper();
    }

    /**
     * 从JedisPool中获取Jedis实例
     *
     * @return Jedis实例
     * @version:v1.0
     * @author:
     * @date:2013-6-25 上午10:37:35
     */
    public static Jedis getResource() {
        if (pool == null) {
            JedisPoolConfig config = new JedisPoolConfig();
            // 控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
            // 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
            config.setMaxActive(-1);
            // 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
            config.setMaxIdle(3);
            // 表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
            config.setMaxWait(1000 * 60);
            // 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
            config.setTestOnBorrow(true);
            String server = SysPropertyUtils.getInstance().getProperty("bizapp.redis.server", "127.0.0.1");
            int port = Integer.parseInt(SysPropertyUtils.getInstance().getProperty("bizapp.redis.port", "6379"));
            String password = SysPropertyUtils.getInstance().getProperty("bizapp.redis.password", "redis");
            pool = new JedisPool(config, server, port, 30 * 1000, password);

        }
        return pool.getResource();
    }

    /**
     * 返还连接池
     *
     * @param jedis jedis实例
     * @version:v1.0
     * @author:
     * @date:2013-6-25 上午10:37:50
     */
    public static void returnResource(Jedis jedis) {
        if (jedis != null) {
            pool.returnResource(jedis);
        }
    }

    /**
     * 销毁出现异常的jedis实例
     *
     * @param jedis jedis实例
     * @version:v1.0
     * @author:
     * @date:2013-6-25 上午10:44:31
     */
    public static void brokenResource(Jedis jedis) {
        if (jedis != null) {
            pool.returnBrokenResource(jedis);
        }
    }

    /**
     * 给指定的key设置过期时间
     *
     * @param key
     * @param seconds
     * @version:v1.0
     * @author:
     * @date:2014-2-7 下午5:24:11
     */
    public static void expire(final String key, final int seconds) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            jedis.expire(key, seconds);
        } catch (Exception e) {
            // 释放redis对象
            brokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnResource(jedis);
        }
    }

    /**
     * 快速取String类型数据
     *
     * @param key
     * @return
     * @version:v1.0
     * @author:
     * @date:2013-6-25 上午10:38:15
     */
    public static String get(String key) {
        String value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            value = jedis.get(key);
        } catch (Exception e) {
            // 释放redis对象
            brokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnResource(jedis);
        }

        return value;
    }

    public static List<String> mget(final String... keys) {
        List<String> resList = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            resList = jedis.mget(keys);
        } catch (Exception e) {
            // 释放redis对象
            brokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnResource(jedis);
        }
        return resList;
    }

    /**
     * 快速存String类型数据
     *
     * @param key
     * @param value
     * @return 操作状态
     * @version:v1.0
     * @author:
     * @date:2013-6-25 上午11:04:54
     */
    public static String set(String key, String value) {
        Jedis jedis = null;
        String statusCode = null;
        try {
            jedis = getResource();
            statusCode = jedis.set(key, value);
        } catch (Exception e) {
            // 释放redis对象
            brokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnResource(jedis);
        }

        return statusCode;
    }

    public static Long incr(String key) {
        Jedis jedis = null;
        Long res = null;
        try {
            jedis = getResource();
            res = jedis.incr(key);
        } catch (Exception e) {
            // 释放redis对象
            brokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnResource(jedis);
        }

        return res;
    }

    /**
     * 存入hash表
     *
     * @param key   hash表名
     * @param field 字段
     * @param value 值
     * @return 更新返回 0,新增返回 1
     * @version:v1.0
     * @author:
     * @date:2013-6-26 下午2:09:17
     */
    public static long hset(String key, String field, String value) {
        Jedis jedis = null;
        long statusCode = -1;
        try {
            jedis = getResource();
            statusCode = jedis.hset(key, field, value);
        } catch (Exception e) {
            // 释放redis对象
            brokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnResource(jedis);
        }

        return statusCode;
    }

    /**
     * 从hash表取数据
     *
     * @param key   hash表名
     * @param field 字段
     * @return
     * @version:v1.0
     * @author:
     * @date:2013-6-26 下午2:18:33
     */
    public static String hget(String key, String field) {
        String value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            value = jedis.hget(key, field);
        } catch (Exception e) {
            // 释放redis对象
            brokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnResource(jedis);
        }

        return value;
    }

    public static Long hlen(String key) {
        Long value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            value = jedis.hlen(key);
        } catch (Exception e) {
            // 释放redis对象
            brokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnResource(jedis);
        }

        return value;
    }

    public static String hmset(final String key, final Map<String, String> hash) {
        String value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            value = jedis.hmset(key, hash);
        } catch (Exception e) {
            // 释放redis对象
            brokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnResource(jedis);
        }

        return value;
    }

    public static List<String> hmget(String key, String... fields) {
        List<String> value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            value = jedis.hmget(key, fields);
        } catch (Exception e) {
            // 释放redis对象
            brokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnResource(jedis);
        }

        return value;
    }

    /**
     * 获得hash中所有的键值对
     *
     * @param key
     * @return
     * @version:v1.0
     * @author:
     * @date:2013-7-4 下午1:20:32
     */
    public static Map<String, String> hgetAll(String key) {
        Map<String, String> map = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            map = jedis.hgetAll(key);
        } catch (Exception e) {
            // 释放redis对象
            brokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnResource(jedis);
        }

        return map;
    }

    /**
     * 获取hash中所有key
     *
     * @param key
     * @return
     * @version:v1.0
     * @author:
     * @date:2013-8-5 下午3:59:59
     */
    public static Set<String> hkeys(String key) {
        Set<String> set = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            set = jedis.hkeys(key);
        } catch (Exception e) {
            // 释放redis对象
            brokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnResource(jedis);
        }

        return set;
    }

    public static List<String> hvals(String key) {
        List<String> list = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            list = jedis.hvals(key);
        } catch (Exception e) {
            // 释放redis对象
            brokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnResource(jedis);
        }

        return list;
    }

    /**
     * 从hash表取数据
     *
     * @param key    hash表名
     * @param fields 字段
     * @return
     * @version:v1.0
     * @author:
     * @date:2013-6-26 下午2:18:33
     */
    public static Long hdel(String key, String... fields) {
        Long value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            value = jedis.hdel(key, fields);
        } catch (Exception e) {
            // 释放redis对象
            brokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnResource(jedis);
        }

        return value;
    }

    public static Long hincrBy(String key, String field, int value) {
        Long res = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            res = jedis.hincrBy(key, field, value);
        } catch (Exception e) {
            // 释放redis对象
            brokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnResource(jedis);
        }
        return res;
    }

    /**
     * @param key
     * @param members
     * @return 添加成功返回0，要添加的对象已存在返回1
     * @version:v1.0
     * @author:
     * @date:2013-6-26 下午2:26:01
     */
    public static long sadd(String key, String... members) {
        Jedis jedis = null;
        long statusCode = -1;
        try {
            jedis = getResource();
            statusCode = jedis.sadd(key, members);
        } catch (Exception e) {
            // 释放redis对象
            brokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnResource(jedis);
        }

        return statusCode;
    }

    /**
     * @param key
     * @return
     * @version:v1.0
     * @author:
     * @date:2013-6-26 下午2:32:16
     */
    public static Set<String> smembers(String key) {
        Jedis jedis = null;
        Set<String> members = null;
        try {
            jedis = getResource();
            members = jedis.smembers(key);
        } catch (Exception e) {
            // 释放redis对象
            brokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnResource(jedis);
        }

        return members;
    }

    /**
     * @param key
     * @param member
     * @return 添加成功返回0，要添加的对象已存在返回1
     * @version:v1.0
     * @author:
     * @date:2013-6-26 下午2:26:01
     */
    public static long zadd(String key, double score, String member) {
        Jedis jedis = null;
        long statusCode = -1;
        try {
            jedis = getResource();
            statusCode = jedis.zadd(key, score, member);
        } catch (Exception e) {
            // 释放redis对象
            brokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnResource(jedis);
        }

        return statusCode;
    }

    public static long zrem(String key, String... member) {
        Jedis jedis = null;
        long statusCode = -1;
        try {
            jedis = getResource();
            statusCode = jedis.zrem(key, member);
        } catch (Exception e) {
            // 释放redis对象
            brokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnResource(jedis);
        }

        return statusCode;
    }

    /**
     * @param key
     * @return
     * @version:v1.0
     * @author:
     * @date:2013-6-26 下午2:32:16
     */
    public static Set<String> zrevrangeByScore(final String key,
                                               final String max, final String min, final int offset,
                                               final int count) {
        Jedis jedis = null;
        Set<String> members = null;
        try {
            jedis = getResource();
            members = jedis.zrevrangeByScore(key, max, min, offset, count);
        } catch (Exception e) {
            // 释放redis对象
            brokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnResource(jedis);
        }

        return members;
    }

    public static Set<String> zrangeByScore(final String key, final long min,
                                            final long max) {
        Jedis jedis = null;
        Set<String> members = null;
        try {
            jedis = getResource();
            members = jedis.zrangeByScore(key, min, max);
        } catch (Exception e) {
            // 释放redis对象
            brokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnResource(jedis);
        }

        return members;
    }

    public static long zremrangeByScore(final String key, final long start,
                                        final long end) {
        Jedis jedis = null;
        long value = 0l;
        try {
            jedis = getResource();
            value = jedis.zremrangeByScore(key, start, end);
        } catch (Exception e) {
            // 释放redis对象
            brokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnResource(jedis);
        }
        return value;
    }

    /**
     * 在list头部添加
     *
     * @param key
     * @param members
     * @return 元素添加完成后list的长度
     * @version:v1.0
     * @author:
     * @date:2013-6-26 下午2:43:02
     */
    public static long lpush(String key, String... members) {
        Jedis jedis = null;
        long length = -1;
        try {
            jedis = getResource();
            length = jedis.lpush(key, members);
        } catch (Exception e) {
            // 释放redis对象
            brokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnResource(jedis);
        }

        return length;
    }

    /**
     * 在list尾部取值并删除最后一条
     *
     * @param key
     * @param members
     * @return 元素添加完成后list的长度
     * @version:v1.0
     * @author:
     * @date:2013-6-26 下午2:43:02
     */
    public static String rpop(String key) {
        Jedis jedis = null;
        String str = null;
        try {
            jedis = getResource();
            str = jedis.rpop(key);
        } catch (Exception e) {
            // 释放redis对象
            brokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnResource(jedis);
        }

        return str;
    }


    /**
     * 是否存在所给的key值
     *
     * @param key
     * @return 验证结果
     * @version:v1.0
     * @author:
     * @date:2013-6-25 上午11:06:59
     */
    public static boolean exists(String key) {
        boolean value = false;
        Jedis jedis = null;
        try {
            jedis = getResource();
            value = jedis.exists(key);
        } catch (Exception e) {
            // 释放redis对象
            brokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnResource(jedis);
        }

        return value;
    }

    /**
     * 删除所给Keys,如果所给的keys中有不存在的key，则不会对那些不存在的key执行任何操作
     *
     * @param keys
     * @return 删除key的个数
     * @version:v1.0
     * @author:
     * @date:2013-6-25 上午11:24:36
     */
    public static long delete(String... keys) {
        long value = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            value = jedis.del(keys);
        } catch (Exception e) {
            // 释放redis对象
            brokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnResource(jedis);
        }

        return value;
    }

    /**
     * key所对应的存储类型
     *
     * @param key
     * @return"none"/"string"/"list"/"set"/"zset"/"hash"
     * @version:v1.0
     * @author:
     * @date:2013-6-25 下午1:14:59
     */
    public static String type(String key) {
        String type = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            type = jedis.type(key);
        } catch (Exception e) {
            // 释放redis对象
            brokenResource(jedis);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnResource(jedis);
        }

        return type;
    }


    //////////////////////////////////////////////////////////////
    /**
     * 设置对象
     * @param key
     * @param obj
     */
//    public static void setObject(String key ,Object obj){
//
//    	Jedis jedis = null;
//
//        try {
//            jedis = getResource();
//            obj = obj == null ? new Object():obj;
//             jedis.set(key.getBytes(), SerializeUtil.serialize(obj));
//        } catch (Exception e) {
//            // 释放redis对象
//            brokenResource(jedis);
//            e.printStackTrace();
//        } finally {
//            // 返还到连接池
//            returnResource(jedis);
//        }
//
//
//
// }
//
//    /**
//     * 获取对象
//     * @param key
//     * @return Object
//     */
// public static Object getObject(String key){
//	 Object object = null ;
//	 Jedis jedis = null;
//
//     try {
//         jedis = getResource();
//         if  ( !jedis.exists(key)){
//        	 return null ;
//         }
//         byte[] data = jedis.get(key.getBytes());
//         object = (Object)SerializeUtil.unserialize(data) ;
//
//
//     } catch (Exception e) {
//         // 释放redis对象
//         brokenResource(jedis);
//         e.printStackTrace();
//     } finally {
//         // 返还到连接池
//         returnResource(jedis);
//     }
//	 return object ;
//
// }

// public static void setObject(String key ,Object obj){
//
// 	Jedis jedis = null;
//
//     try {
//         jedis = getResource();
//         obj = obj == null ? new Object():obj;
//
//         String objStr = om.writeValueAsString(obj);
//           jedis.set(key, objStr);
//     } catch (Exception e) {
//         // 释放redis对象
//         brokenResource(jedis);
//         e.printStackTrace();
//     } finally {
//         // 返还到连接池
//         returnResource(jedis);
//     }
//


//}

    /**
     * 获取对象
     * @param key
     * @return Object
     */
//public static Object getObject(String key){
//	 Object object = null ;
//	 Jedis jedis = null;
//
//  try {
//      jedis = getResource();
//      if  ( !jedis.exists(key)){
//     	 return null ;
//      }
//      String strvalue = jedis.get(key);
//
//
//
//    if (strvalue != null) {
//    	object = om.readValue(strvalue, Object.class);
//    }
//
//  } catch (Exception e) {
//      // 释放redis对象
//      brokenResource(jedis);
//      e.printStackTrace();
//  } finally {
//      // 返还到连接池
//      returnResource(jedis);
//  }
//	 return object ;
//
//}


// /**
//     * 设置List集合
//     * @param key
//     * @param list
//     */
//    public static void setList(String key ,List<?> list){
//
//
//    	Jedis jedis = null;
//      	try {
//    		jedis = getResource();
//         if(!(ListUtils.isNull(list))){
//        	 jedis.set(key.getBytes(), SerializeUtil.serializeList(list));
//         }else{//如果list为空,则设置一个空
//        	 jedis.set(key.getBytes(), "".getBytes());
//         }
//     } catch (Exception e) {
//         e.printStackTrace();
//     }
// }
//
//    /**
//     * 获取List集合
//     * @param key
//     * @return
//     */
// public static List<?> getList(String key){
//
//	 List<?> list =null ;
//	 Jedis jedis = null;
//
//     try {
//         jedis = getResource();
//         if  ( !jedis.exists(key)){
//        	 return null ;
//         }
//         byte[] data = jedis.get(key.getBytes());
//
//         list =SerializeUtil.unserializeList(data);
//
//     } catch (Exception e) {
//         // 释放redis对象
//         brokenResource(jedis);
//         e.printStackTrace();
//     } finally {
//         // 返还到连接池
//         returnResource(jedis);
//     }
//	 return list ;
//
//
// }


// public static void main(String[] args) {
//     //object
//     setObject("100",new Person("caspar",25));
//
//     Person p = (Person)getObject("100");
//     System.out.println(p.getName()+"----"+p.getAge());
//
//     //list
//     List<Person> list = new ArrayList<Person>();
//     list.add(new Person("唐马儒",39));
//     list.add(new Person("大便熊",33));
//     list.add(new Person("小萝莉",14));
//
//     setList("list001", list);
//     List<Person> resultList = (List<Person>) getList("list001");
//     for (Person person : resultList) {
//         System.out.println(person.getName()+"----"+person.getAge());
//     }
// }


}
