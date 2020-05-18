package com.feel.common.utils.generate;

public interface SerialNoGenerator<T> extends IDGenerator<T> {

    /**
     * 限购订单号生成
     */
    IDGenerator<String> LIMIT_SERIAL_NO = () -> String.format("L%s",String.valueOf(SNOW_FLAKE.generate()));

    /**
     * 秒杀订单号生成
     */
    IDGenerator<String> SECKILL_SERIAL_NO = () -> String.format("S%s",String.valueOf(SNOW_FLAKE.generate()));

    /**
     * 充值单号生成
     */
    IDGenerator<String> CHARGE_SERIAL_NO = () -> String.format("C%s",String.valueOf(SNOW_FLAKE.generate()));

    /**
     * 提现单号生成
     */
    IDGenerator<String> CASH_SERIAL_NO = () -> String.format("D%s",String.valueOf(SNOW_FLAKE.generate()));

    /**
     * 互转单号生成
     */
    IDGenerator<String> EXCHANG_SERIAL_NO = () -> String.format("E%s",String.valueOf(SNOW_FLAKE.generate()));

}
