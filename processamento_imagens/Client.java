package processamento_imagens;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {

	public static void main(String[] args) {

		for (int i = 1; i <= 2; i++) {
			
			String directory = "C:/Users/User/eclipse-workspace/Imagens/src/processamento_imagens/envio/" + i;
			List<String> paths = new ArrayList<>();
			File folder = new File(directory);
			File[] files = folder.listFiles();

			for (File file : files) {
				paths.add(file.getAbsolutePath());
			}
			try (Socket socket = new Socket("127.0.0.1", 5000)) {

				DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
				DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
				dataOutputStream.writeInt(files.length);

				for (String path : paths) {	
					sendFile(path, dataInputStream, dataOutputStream);
				}
				
				BufferedReader readServer = new BufferedReader(new InputStreamReader(dataInputStream));
		        String serverMessage = readServer.readLine();
		        System.out.println(serverMessage);
				
		        dataInputStream.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static void sendFile(String path, DataInputStream dataInputStream, DataOutputStream dataOutputStream) throws Exception {
		int bytes = 0;
		File file = new File(path);
		FileInputStream fileInputStream = new FileInputStream(file);
		dataOutputStream.writeLong(file.length());
		byte[] buffer = new byte[4 * 1024];
		while ((bytes = fileInputStream.read(buffer)) != -1) {
			dataOutputStream.write(buffer, 0, bytes);
			dataOutputStream.flush();
		}
		fileInputStream.close();
	}
}
