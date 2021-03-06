package com.linyimin.java.distributed.service.framework.rpc;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @Author: linyimin
 * @Description:
 * @Date: Created in 上午10:32 18-7-9
 */
public class RpcExporter {
    static Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    public static void exporter(String hostName, int port) throws IOException {
        ServerSocket server = new ServerSocket();
        server.bind(new InetSocketAddress(hostName, port));
        try{
            while (true){
                executor.execute(new ExporterTask(server.accept()));
            }
        }finally {
            server.close();
        }
    }
    private static class ExporterTask implements Runnable{
        private Socket client= null;

        public ExporterTask(Socket client) {
            this.client = client;
        }
        @Override
        public void run() {
            ObjectInputStream input = null;
            ObjectOutputStream output = null;

            try{
                input = new ObjectInputStream(client.getInputStream());
                String interfaceName = input.readUTF();
                Class<?> service = Class.forName(interfaceName);
                String methodName = input.readUTF();
                Class<?>[] parameterTypes = (Class<?>[]) input.readObject();
                Object[] arguments = (Object[]) input.readObject();
                Method method = service.getMethod(methodName, parameterTypes);
                Object result = method.invoke(service.newInstance(), arguments);
                output = new ObjectOutputStream(client.getOutputStream());
                output.writeObject(result);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }finally {
                try{
                    if (output != null) {
                        output.close();
                    }
                    if (input != null) {
                        input.close();
                    }
                    if (client != null) {
                        client.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}

