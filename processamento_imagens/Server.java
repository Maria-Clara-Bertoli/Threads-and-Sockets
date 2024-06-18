package processamento_imagens;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutorService;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	private static ExecutorService executorService = Executors.newCachedThreadPool();
	private static int id = 0;

	public static void main(String[] args) {
		try (ServerSocket serverSocket = new ServerSocket(5000)) {
			System.out.println("Servidor iniciado e aguardando conexões...");

			while (true) {
				id = id + 1;
				Socket clientSocket = serverSocket.accept();
				System.out.println(clientSocket + " conectado.");
				System.out.println();

				try {
					DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
					DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
					int readInt = dataInputStream.readInt();

					for (int i = 1; i <= readInt; i++) {

						String path = "C:/Users/User/eclipse-workspace/Imagens/src/processamento_imagens/recebimento/"
								+ id + i + ".jpg";
						receiveFile(path, dataInputStream, dataOutputStream);
						
						executorService.execute(() -> {
				            try {
				        			Processing task = new Processing(path);
				        			task.run();
				        			
				        			
				            } catch (Exception e) {
				                e.printStackTrace();
				            }
				        });
					}
					
					PrintWriter message = new PrintWriter(dataOutputStream, true);
			        message.println("Processamento Concluído!"); 
					
				     dataInputStream.close();
				     dataOutputStream.close();
				     clientSocket.close();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void receiveFile(String path, DataInputStream dataInputStream, DataOutputStream dataOutputStream)
			throws Exception {
		int bytes = 0;
		FileOutputStream fileOutputStream = new FileOutputStream(path);
		long size = dataInputStream.readLong();
		byte[] buffer = new byte[4 * 1024];
		while (size > 0 && (bytes = dataInputStream.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
			fileOutputStream.write(buffer, 0, bytes);
			size -= bytes;
		}
		fileOutputStream.close();
	}
}
