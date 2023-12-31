/*
// thanks to : https://gist.github.com/fjavieralba/7930018
package x.utils.archived;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;

public class ZooKeeperLocal {

  ZooKeeperServerMain zooKeeperServer;

  public ZooKeeperLocal(Properties zkProperties)
      throws FileNotFoundException, IOException {
    QuorumPeerConfig quorumConfiguration = new QuorumPeerConfig();
    try {
      quorumConfiguration.parseProperties(zkProperties);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    zooKeeperServer = new ZooKeeperServerMain();
    final ServerConfig configuration = new ServerConfig();
    configuration.readFrom(quorumConfiguration);

    new Thread() {
      public void run() {
        try {
          zooKeeperServer.runFromConfig(configuration);
        } catch (IOException e) {
          System.out.println("ZooKeeper Failed");
          e.printStackTrace(System.err);
        }
      }
    }.start();
  }
}
*/