import com.alex.zero.Dir;
import com.alex.zero.Group;
import com.alex.zero.net.TankMsg;
import com.alex.zero.net.TankMsgDecoder;
import com.alex.zero.net.TankMsgEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import java.util.UUID;

/**
 * @author Alexander Zero
 * @version 1.0.0
 * @date 2020/11/10
 * @description
 * 编码/解码 器测试
 */
public class CodecTest {

    @Test
    public void encoderTest() {
        EmbeddedChannel channel = new EmbeddedChannel(new TankMsgEncoder());
        UUID id = UUID.randomUUID();
        TankMsg tankMsg = new TankMsg(10, 13, Dir.LEFT, false, Group.GOOD, id);
        channel.writeOutbound(tankMsg);

        ByteBuf buf = (ByteBuf) channel.readOutbound();
        int x = buf.readInt();
        int y = buf.readInt();
        boolean moving = buf.readBoolean();
        int dir = buf.readInt();
        int group = buf.readInt();
        long high = buf.readLong();
        long low = buf.readLong();

        assert x == 10;
        assert y == 13;
        assert Dir.values()[dir] == Dir.LEFT;
        assert moving == false;
        assert Group.values()[group] == Group.GOOD;
        assert id.equals(new UUID(high, low));

    }

    @Test
    public void decoderTest() {
        EmbeddedChannel channel =new EmbeddedChannel(new TankMsgDecoder());
        ByteBuf byteBuf = Unpooled.buffer();
        UUID id = UUID.randomUUID();
        TankMsg tankMsg = new TankMsg(10, 13, Dir.LEFT, false, Group.GOOD, id);
        byteBuf.writeInt(tankMsg.x);
        byteBuf.writeInt(tankMsg.y);
        byteBuf.writeBoolean(tankMsg.moving);
        byteBuf.writeInt(tankMsg.dir.ordinal());
        byteBuf.writeInt(tankMsg.group.ordinal());
        byteBuf.writeLong(tankMsg.id.getMostSignificantBits());
        byteBuf.writeLong(tankMsg.id.getLeastSignificantBits());

        channel.writeInbound(byteBuf);
        TankMsg msg = (TankMsg) channel.readInbound();

        assert msg.equals(tankMsg);


    }

}
