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
 * @date 2020/11/11
 * @description
 */
public class BulletFireCoderTest {

    @Test
    public void encoderTest() {
        EmbeddedChannel channel = new EmbeddedChannel();
        channel.pipeline().addLast(new MsgEncoder());

        BulletFireMsg msg = new BulletFireMsg(10, 13, Dir.LEFT, Group.GOOD);
        channel.writeOutbound(msg);

        ByteBuf buf = (ByteBuf) channel.readOutbound();
        MsgType type = MsgType.values()[buf.readInt()];
        int size = buf.readInt();
        byte[] bytes = new byte[size];
        buf.readBytes(bytes);

        BulletFireMsg bulletFireMsg = new BulletFireMsg();
        bulletFireMsg.parse(bytes);

        assert type == msg.getMsgType();
        assert msg.equals(bulletFireMsg);

    }

    @Test
    public void decoderTest() {
        EmbeddedChannel channel = new EmbeddedChannel(new MsgDecoder());
        ByteBuf byteBuf = Unpooled.buffer();
        UUID id = UUID.randomUUID();
        BulletFireMsg msg = new BulletFireMsg(10, 13, Dir.LEFT, Group.GOOD);
        byte[] bytes = msg.getBytes();

        byteBuf.writeInt(msg.getMsgType().ordinal());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);

        channel.writeInbound(byteBuf.duplicate());
        Msg readMsg = (Msg) channel.readInbound();

        assert readMsg.getMsgType() == readMsg.getMsgType();
        assert readMsg.equals(readMsg);


    }
}
