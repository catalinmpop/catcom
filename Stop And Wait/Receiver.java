//CP372 popx4690 A2 RECIEVER SAW

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Receiver {
	public static void main(String[] args) throws IOException {
		if (args.length != 5) {
			System.exit(0);
		}
        if (Integer.parseInt(args[3]) < 0) {
            System.out.println("RN is invalid. It must be greater than or equal to 0.");
            System.exit(0);
        }
		DatagramSocket socket = null;
		DatagramPacket sendPacket = null;
		DatagramPacket recievePacket = null;
        int rn = Integer.parseInt(args[3]);
		int seqNum = 0;
		String dat;
		int left;
		byte[] data = new byte[1024];
		byte[] fdata = new byte[1016];
		byte[] ack = new byte[3];
		byte[] lNum = new byte[4];
        int c = 0, a = 1;
		try {
			InetAddress address = InetAddress.getByName(args[0]);
			socket = new DatagramSocket(Integer.parseInt(args[2]));
			RandomAccessFile f = new RandomAccessFile(args[4], "rw");
			System.out.println("Waiting... for data\n\n");
			while (true) {

				recievePacket = new DatagramPacket(data, data.length);
				socket.receive(recievePacket);
				dat = new String(data, "UTF-8");
				System.out.println(dat);
				if (dat.endsWith("EOF")) {
					System.arraycopy(data, 1017, lNum, 0, lNum.length);
					left = Integer.parseInt(new String(lNum, "UTF-8"));
					System.out.println(Integer.toString(left));
					fdata = new byte[left];
					System.arraycopy(data, 0, fdata, 0, left);
					f.write(fdata);
					ack = "ACK".getBytes();
					sendPacket = new DatagramPacket(ack, ack.length, address,
							Integer.parseInt(args[1]));
					socket.send(sendPacket);
					break;
				} else if (dat.endsWith(Integer.toString(seqNum))) {
					ack = "ACK".getBytes();
					dat = dat.substring(0, dat.length() - Integer.toString(seqNum).length());
					fdata = new byte[1016];
					System.arraycopy(data, 0, fdata, 0, fdata.length);
					f.write(fdata);
					seqNum++;
					seqNum = seqNum % 2;
				} else {
					ack = "NAK".getBytes();
				}
				sendPacket = new DatagramPacket(ack, ack.length, address, Integer.parseInt(args[1]));
                
                if (rn == 0) {
                    socket.send(sendPacket);
                } else if (rn > 1) {
                    if (c + 1 != a * rn) {
                        socket.send(sendPacket);
                    } else {
                        a++;
                    }
                }
                c++;
			}
			f.close();
			System.out.println("All data successfully received and stored in "+args[4]);

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				socket.close();

			} catch (Exception e) {
			}
		}
	}
}