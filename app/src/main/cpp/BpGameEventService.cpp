/* 参考: frameworks\av\media\libmedia\IMediaPlayerService.cpp */

#include "IGameEventService.h"

namespace android {

class BpGameEventService: public BpInterface<IGameEventService>
{
	public:
		BpGameEventService(const sp<IBinder>& impl)
			: BpInterface<IGameEventService>(impl)
		{
		}
		int startService(void)
		{
			/* 构造/发送数据 */
			Parcel data, reply;
			data.writeInt32(0);

			remote()->transact(GAME_EVENT_START_SERVICE, data, &reply);
			
			return reply.readInt32();
		}
		
		int stopService(void)
		{
			/* 构造/发送数据 */
			Parcel data, reply;
			data.writeInt32(0);

			remote()->transact(GAME_EVENT_STOP_SERVICE, data, &reply);
			
			return reply.readInt32();
		}

		int controlService(const char *buf)
		{
			/* 构造/发送数据 */
			Parcel data, reply;
			data.writeInt32(0);
			data.writeString16(String16(buf));

			remote()->transact(GAME_EVENT_CONTROL_SERVICE, data, &reply);
			
			return reply.readInt32();
		}

};

IMPLEMENT_META_INTERFACE(GameEventService, "android.media.IGameEventService");

}


