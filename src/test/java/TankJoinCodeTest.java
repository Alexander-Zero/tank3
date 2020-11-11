import com.alex.zero.Dir;
import com.alex.zero.Group;
import com.alex.zero.net.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import java.util.UUID;

/**
 * @author Alexander Zero
 * @version 1.0.0
 * @date 2020/11/10
 * @description 编码/解码 器测试
 */
public class TankJoinCodeTest {

    @Test
    public void encoderTest() {
        EmbeddedChannel channel = new EmbeddedChannel();
        channel.pipeline().addLast(new MsgEncoder());
        channel.pipeline().addLast(new MsgDecoder());
        UUID id = UUID.randomUUID();
        TankJoinMsg tankJoinMsg = new TankJoinMsg(10, 13, Dir.LEFT, false, Group.GOOD, id);
        channel.writeOutbound(tankJoinMsg);

        ByteBuf buf = (ByteBuf) channel.readOutbound();
        MsgType type = MsgType.values()[buf.readInt()];
        int size = buf.readInt();
        byte[] bytes = new byte[size];
        buf.readBytes(bytes);

        TankJoinMsg tankJoinMsg1 = new TankJoinMsg();
        tankJoinMsg1.parse(bytes);

        assert type == tankJoinMsg.getMsgType();
        assert tankJoinMsg.equals(tankJoinMsg1);

    }

    @Test
    public void decoderTest() {
        EmbeddedChannel channel = new EmbeddedChannel(new MsgDecoder());
        ByteBuf byteBuf = Unpooled.buffer();
        UUID id = UUID.randomUUID();
        TankJoinMsg tankJoinMsg = new TankJoinMsg(10, 13, Dir.LEFT, false, Group.GOOD, id);
        byte[] bytes = tankJoinMsg.getBytes();

        byteBuf.writeInt(tankJoinMsg.getMsgType().ordinal());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);

        channel.writeInbound(byteBuf.duplicate());
        Msg msg = (Msg) channel.readInbound();

        assert msg.getMsgType() == tankJoinMsg.getMsgType();
        assert msg.equals(tankJoinMsg);


    }

}
