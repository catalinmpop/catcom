// CP372 popx4690 A2 SENDER SAW

import java.io.RandomAccessFile;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class Sender {

	public static void main(String[] args) {

		if (args.length != 4) {
			System.exit(0);
		}
		DatagramSocket socket = null;
		DatagramPacket sendPacket = null;
		DatagramPacket recievePacket = null;
		int sn = 0;
		byte[] seqNum = new byte[1];
		byte[] temp = new byte[1016];
		byte[] buf;
		byte[] ack = new byte[3];
		byte[] lNum = new byte[4];
		byte[] eOfF = new byte[3];
		eOfF = "EOF".getBytes();
		String ak;
		long tTT = 0;
		boolean continueSending = true;
		long start = System.currentTimeMillis();
        int c = 0;

		boolean eof = false;
		int left;
		try {

			RandomAccessFile f = new RandomAccessFile(args[3], "r");
			byte[] file = new byte[(int) f.length()];
			int l = (int) f.length();
			int curL = 0;
			f.readFully(file);
			f.close();
			InetAddress address = InetAddress.getByName(args[0]);
			socket = new DatagramSocket(Integer.parseInt(args[2]));

			while (!eof) {
				temp = new byte[1016];

				seqNum = Integer.toString(sn).getBytes();
				left = l - curL;
				if (left < 1016) {
					if (left < 10) {
						lNum = ("000" + Integer.toString(left)).getBytes();
					} else if (left < 100 && left > 9) {
						lNum = ("00" + Integer.toString(left)).getBytes();
					} else if (left < 1000 && left > 99) {
						lNum = ("0" + Integer.toString(left)).getBytes();
					} else {
						lNum = Integer.toString(left).getBytes();
					}
					System.arraycopy(file, 1016 * c, temp, 0, left);
					buf = new byte[temp.length + seqNum.length + lNum.length + eOfF.length];
					System.arraycopy(temp, 0, buf, 0, temp.length);
					System.arraycopy(seqNum, 0, buf, temp.length, seqNum.length);
					System.arraycopy(lNum, 0, buf, temp.length + seqNum.length, lNum.length);
					System.arraycopy(eOfF, 0, buf, temp.length + seqNum.length + lNum.length,
							eOfF.length);
					eof = true;
				} else {
					System.arraycopy(file, 1016 * c, temp, 0, temp.length);
					buf = new byte[1024];
					System.arraycopy(temp, 0, buf, 0, temp.length);
					System.arraycopy(seqNum, 0, buf, temp.length + 7, seqNum.length);
				}

				sendPacket = new DatagramPacket(buf, buf.length, address, Integer.parseInt(args[1]));
				System.out.println(new String(sendPacket.getData(), 0, sendPacket.getLength()));
                
                socket.send(sendPacket);

				socket.setSoTimeout(200);
				while (continueSending) {
					recievePacket = new DatagramPacket(ack, ack.length);
					try {
						socket.receive(recievePacket);
						ak = new String(recievePacket.getData(), 0, recievePacket.getLength());
						System.out.println(ak);
						if (ak.equals("ACK") || ak.equals("NAK")) {
							continueSending = false;
						}

					} catch (SocketTimeoutException e) {
						socket.send(sendPacket); // send again when timeout
						socket.setSoTimeout(200);
						continue;
					}
				}
				sn++;
				sn %= 2;
				c++;
				curL += 1016;
				continueSending = true;
			}

			System.out.println("All data succsessfully sent from "+args[4]);
			tTT = System.currentTimeMillis() - start;
			System.out.println("Time (ms):" + tTT);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				socket.close();

			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}
}
