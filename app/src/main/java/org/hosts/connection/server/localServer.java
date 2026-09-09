package org.hosts.connection.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public final class localServer {
	public interface Listener {
		void onStarted(int port);
		void onError(Exception error);
	}

	public interface LogListener {
		void onLog(String log);
	}

	private final int port;
	private final String title;
	private final Listener listener;
	private final List<String> logs = new ArrayList<>();
	private final List<LogListener> logListeners = new ArrayList<>();
	private volatile boolean running;
	private ServerSocket serverSocket;
	private Thread serverThread;

	public localServer(int port, Listener listener) {
		this(port, "hosts", listener);
	}

	public localServer(int port, String title, Listener listener) {
		this.port = port;
		this.title = title == null || title.trim().isEmpty() ? "hosts" : title.trim();
		this.listener = listener;
	}

	public synchronized void start() {
		if (running) return;
		running = true;
		serverThread = new Thread(() -> {
			try {
				serverSocket = new ServerSocket(port);
				addLog("Server started on port " + serverSocket.getLocalPort());
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
				addLog("Server error: " + error.getMessage());
				listener.onError(error);
			}
		}, "hosts-local-server");
		serverThread.start();
	}

	private void handle(Socket client) {
		String clientAddress = client.getInetAddress().getHostAddress();
		String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
		String logEntry = timestamp + " - " + clientAddress;
		try (Socket socket = client;
			 BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			 OutputStream output = socket.getOutputStream()) {
			reader.readLine();
			String header;
			String requestLine = "";
			while ((header = reader.readLine()) != null && !header.isEmpty()) {
				if (requestLine.isEmpty()) requestLine = header;
			}
			logEntry += " -> " + requestLine;
			String body = "<html><head><title>" + title + "</title></head>"
					+ "<body style='font-family:sans-serif;padding:2rem'>"
					+ "<h1>" + title + "</h1><p>Server lokal aktif.</p></body></html>";
			byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
			String response = "HTTP/1.1 200 OK\r\nContent-Type: text/html; charset=utf-8\r\n"
					+ "Content-Length: " + bytes.length + "\r\nConnection: close\r\n\r\n";
			output.write(response.getBytes(StandardCharsets.UTF_8));
			output.write(bytes);
			output.flush();
			addLog(logEntry + " [200]");
		} catch (IOException ignored) {
			addLog(logEntry + " [ERROR]");
		}
	}

	private void addLog(String log) {
		logs.add(log);
		for (LogListener listener : logListeners) {
			listener.onLog(log);
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
		addLog("Server stopped");
	}

	public boolean isRunning() {
		return running;
	}

	public int getPort() {
		return port;
	}

	public List<String> getLogs() {
		return new ArrayList<>(logs);
	}

	public void addLogListener(LogListener listener) {
		logListeners.add(listener);
	}

	public void removeLogListener(LogListener listener) {
		logListeners.remove(listener);
	}
}
