/**
 * Created by robotics on 2/9/2016.
 */
package network

import network.packets.Packet01
import java.io.EOFException
import java.io.IOException
import java.io.ObjectInputStream
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException

/**
 * Created by robotics on 2/8/2016.
 */
class MainServer {


    init {
        val serverSocket: ServerSocket
        try {
            serverSocket = ServerSocket(7093, 3)
            var client: Socket
            while (true) {
                client = serverSocket.accept();
                try {
                    ObjectInputStream(client.inputStream).use { objectInputStream ->
                        while (client.isConnected) {
                            try {
                                val unknown = objectInputStream.readObject()
                                when (unknown) {
                                    is Packet01 -> {
                                        println("R: Packet 1 | ${unknown.dataType} | ${unknown.extra}")
                                    }
                                }
                            } catch (ex: EOFException) {

                            } catch(e2: SocketException) {
                                break;
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    break
                }

            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    companion object {

        @JvmStatic fun main(args: Array<String>) {
            MainServer()
        }
    }
}
