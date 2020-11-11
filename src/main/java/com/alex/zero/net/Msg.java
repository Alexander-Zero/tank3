package com.alex.zero.net;

/**
 * @author Alexander Zero
 * @version 1.0.0
 * @date 2020/11/11
 * @description
 */
public abstract class Msg {
    /**
     * 对返回来的消息如何处理
     */
    public abstract void handler();

    /**
     * 将消息转换为字节数组,供编码所用
     *
     * @return
     */
    public abstract byte[] getBytes();

    /**
     * 将字节数据 转换为消息内的属性值
     *
     * @param bytes
     */
    public abstract void parse(byte[] bytes);

    /**
     * 获取消息类型
     *
     * @return
     */
    public abstract MsgType getMsgType();
}
