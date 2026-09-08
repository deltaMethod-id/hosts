package org.hosts.connection.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public final class localServer {
	public interface Listener {
		void onStarted(int port);
		void onError(Exception error);
	}

	private final int port;
	private final Listener listener;
	private volatile boolean running;
	private ServerSocket serverSocket;
	private Thread serverThread;

	public localServer(int port, Listener listener) {
		this.port = port;
		this.listener = listener;
	}

	public synchronized void start() {
		if (running) return;
		running = true;
		serverThread = new Thread(() -> {
			try {
				serverSocket = new ServerSocket(port);
				listener.onStarted(serverSocket.getLocalPort());
				while (running) {
					try {
						Socket client = serverSocket.accept();
						handle(client);
					} catch (IOException error) {
						if (running) listener.onError(error);
					}
				}
			} catch (Exception error) {
				running = false;
				listener.onError(error);
			}
		}, "hosts-local-server");
		serverThread.start();
	}

	private void handle(Socket client) {
		try (Socket socket = client;
			 BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			 OutputStream output = socket.getOutputStream()) {
			reader.readLine();
			String header;
			while ((header = reader.readLine()) != null && !header.isEmpty()) {
			}
			String body = "<html><head><title>hosts</title></head>"
					+ "<body style='font-family:sans-serif;padding:2rem'>"
					+ "<h1>hosts</h1><p>Server lokal aktif.</p></body></html>";
			byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
			String response = "HTTP/1.1 200 OK\r\nContent-Type: text/html; charset=utf-8\r\n"
					+ "Content-Length: " + bytes.length + "\r\nConnection: close\r\n\r\n";
			output.write(response.getBytes(StandardCharsets.UTF_8));
			output.write(bytes);
			output.flush();
		} catch (IOException ignored) {
		}
	}

	public synchronized void stop() {
		running = false;
		if (serverSocket != null) {
			try {
				serverSocket.close();
			} catch (IOException ignored) {
			}
		}
		serverSocket = null;
	}

	public boolean isRunning() {
		return running;
	}
}
