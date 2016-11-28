/* 参考: frameworks\av\include\media\IMediaPlayerService.h */

#ifndef __ANDROID_IGAMESERVICE_H_
#define __ANDROID_IGAMESERVICE_H_

#include <pthread.h>

#include <utils/Errors.h>  // for status_t
#include <utils/KeyedVector.h>
#include <utils/RefBase.h>
#include <utils/String8.h>
#include <binder/IInterface.h>
#include <binder/Parcel.h>

#define GAME_EVENT_START_SERVICE	0
#define GAME_EVENT_STOP_SERVICE		1
#define GAME_EVENT_CONTROL_SERVICE	2


#undef LOG_TAG
#define LOG_TAG "TestGameEventService"

namespace android {

class IGameEventService: public IInterface
{
	public:
		DECLARE_META_INTERFACE(GameEventService);

		virtual int startService(void) = 0;
		virtual int stopService(void) = 0;

		virtual int controlService(const char *data) =  0;
};

class BnGameEventService: public BnInterface<IGameEventService>
{
		static int				mCtrlPipe[2];
		pthread_t       mThread;
	
public:
		BnGameEventService();
		
		virtual status_t onTransact(uint32_t code, const Parcel& data,
				Parcel* reply, uint32_t flags = 0);

		int startService(void);
		int stopService(void);

		int controlService(const char *data);
		
	private:
		static void *handle_event_thread(void *obj);

};

}
#endif



