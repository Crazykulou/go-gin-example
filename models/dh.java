case DH:
                    //dh检测在线状态逻辑
                    LLong id = new LLong(a.getLoginUserId());
                    amcsHealthVO.setServiceUp(id.longValue() != 0);
                    //设备状态查询结构体
                    NetSDKLib.NET_IN_GET_CAMERA_STATEINFO stIn = new NetSDKLib.NET_IN_GET_CAMERA_STATEINFO();
                    //查询所有设备状态
                    stIn.bGetAllFlag = 1;
                    int chnCount = a.getChanNum();  // 通道个数
                    NetSDKLib.NET_CAMERA_STATE_INFO[] cameraInfo = new NetSDKLib.NET_CAMERA_STATE_INFO[chnCount];
                    for (int i = 0; i < chnCount; i++) {
                        cameraInfo[i] = new NetSDKLib.NET_CAMERA_STATE_INFO();
                    }
                    NetSDKLib.NET_OUT_GET_CAMERA_STATEINFO stOut = new NetSDKLib.NET_OUT_GET_CAMERA_STATEINFO();
                    stOut.nMaxNum = chnCount;
                    stOut.pCameraStateInfo = new Memory(cameraInfo[0].size() * chnCount);
                    stOut.pCameraStateInfo.clear(cameraInfo[0].size() * chnCount);
                    ToolKits.SetStructArrToPointerData(cameraInfo, stOut.pCameraStateInfo);
                    stIn.write();
                    stOut.write();
                    boolean success = netsdk.CLIENT_QueryDevInfo(id, NetSDKLib.NET_QUERY_GET_CAMERA_STATE,
                            stIn.getPointer(),
                            stOut.getPointer(),
                            null, 5000);
                    stOut.read();
                    if (!success) {
                        log.info(ToolKits.getErrorCodePrint());
                    }
                    List<CameraInfoPojo> dhCameraList = collection.stream().filter(ca -> ca.getNvrSerialNum().equals(a.getSerialNumber())).collect(Collectors.toList());
                    try {
                        ToolKits.GetPointerDataToStructArr(stOut.pCameraStateInfo, cameraInfo);
                        for (int i = 0; i < stOut.nValidNum; i++) {
                            for (CameraInfoPojo c : dhCameraList) {
                                if (cameraInfo[i].nChannel == c.getChannelNum()) {
                                    DeviceHealthVO cameraHealth = new DeviceHealthVO();
                                    cameraHealth.setServiceUp(cameraInfo[i].emConnectionState == 2);
                                    cameraHealth.setName(c.getCameraName());
                                    cameraHealth.setDeviceType(c.getCameraTypeCode());
                                    cameraHealth.setCustomCode(c.getCustomCode());
                                    list.add(cameraHealth);
                                }
                            }
                        }
                        amcsHealthVO.setDeviceList(list);
                        amcsList.add(amcsHealthVO);
                        break;
                    } catch (NullPointerException exception) {
                        // 如果Nvr不支持或检测出错，直接返回全部的失败
                        exception.printStackTrace();
                        for (CameraInfoPojo c : dhCameraList) {
                            DeviceHealthVO cameraHealth = new DeviceHealthVO();
                            cameraHealth.setServiceUp(false);
                            cameraHealth.setName(c.getCameraName());
                            cameraHealth.setDeviceType(c.getCameraTypeCode());
                            cameraHealth.setCustomCode(c.getCustomCode());
                            list.add(cameraHealth);
                        }
                        amcsHealthVO.setDeviceList(list);
                        amcsList.add(amcsHealthVO);
                        break;
                    }