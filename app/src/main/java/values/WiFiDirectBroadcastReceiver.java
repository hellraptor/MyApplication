package values;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;


import com.example.svyatoslav_yakovlev.myapplication.MainActivity;

import java.util.ArrayList;
import java.util.List;


public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private MainActivity mActivity;
    WifiP2pManager.PeerListListener myPeerListListener;

    private WifiP2pManager.GroupInfoListener groupInfoListener;
    private WifiP2pManager.ConnectionInfoListener connectionInfoListener;

    private List<WifiP2pDevice> peers = new ArrayList<>();

    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel,
                                       MainActivity activity) {
        super();
        this.mManager = manager;
        this.mChannel = channel;
        this.mActivity = activity;

        groupInfoListener = new WifiP2pManager.GroupInfoListener() {
            @Override
            public void onGroupInfoAvailable(WifiP2pGroup group) {
                if(group != null) {
                  /*  Log.d("getNetworkName", group.getNetworkName());*/
                  /*  Log.d("getOwner().deviceAddress", group.getOwner().deviceAddress);*/
                  /*  Log.d("getOwner().deviceName", group.getOwner().deviceName);*/
                }
            }
        };

        connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
            @Override
            public void onConnectionInfoAvailable(WifiP2pInfo info) {
                if(info != null && info.groupOwnerAddress != null) {
                    /*
                    Log.d("groupOwnerAddress.toString", info.groupOwnerAddress.toString());
                    Log.d("groupOwnerAddress.getHostAddress", info.groupOwnerAddress.getHostAddress());
                    Log.d("groupOwnerAddress.getHostName", info.groupOwnerAddress.getHostName());
                    Log.d("groupOwnerAddress.getCanonicalHostName", info.groupOwnerAddress.getCanonicalHostName());*/
                }
            }
        };
        myPeerListListener = new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList peerList) {

                List<WifiP2pDevice> refreshedPeers = new ArrayList<>(peerList.getDeviceList());
                if (!refreshedPeers.equals(peers)) {
                    peers.clear();
                    peers.addAll(refreshedPeers);

                    // If an AdapterView is backed by this data, notify it
                    // of the change.  For instance, if you have a ListView of
                    // available peers, trigger an update.
                    //todo ((MainActivity) getListAdapter()).notifyDataSetChanged();

                    // Perform any other updates needed based on the new list of
                    // peers connected to the Wi-Fi P2P network.
                }

                if (peers.size() == 0) {
                    //todo Log.d(MainActivity.TAG, "No devices found");
                    return;
                }
            }
        };



    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
      //todo  Log.d(DEBUG_TAG, "============ "+action);

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            //Log.d(DEBUG_TAG, "WIFI_P2P_STATE_CHANGED_ACTION");
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            //todo Log.d(DEBUG_TAG, "+++++++++++ WIFI_P2P_PEERS_CHANGED_ACTION");
            if(mManager != null) {
                mManager.requestPeers(mChannel, myPeerListListener);
            }
            else {
                //todo  Log.d(DEBUG_TAG, "MANAGER NULL");
            }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            //Log.d(DEBUG_TAG, "WIFI_P2P_CONNECTION_CHANGED_ACTION");
            //get ip address of GO
            if(mManager != null) {
                mManager.requestGroupInfo(mChannel, groupInfoListener);
                mManager.requestConnectionInfo(mChannel, connectionInfoListener);
            }
            else {
                //todo     Log.d(DEBUG_TAG, "MANAGER NULL");
            }
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            //Log.d(DEBUG_TAG, "WIFI_P2P_THIS_DEVICE_CHANGED_ACTION");
        }
    }
}
