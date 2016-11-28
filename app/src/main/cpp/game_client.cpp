
#include <fcntl.h>
#include <sys/prctl.h>
#include <sys/wait.h>
#include <binder/IPCThreadState.h>
#include <binder/ProcessState.h>
#include <binder/IServiceManager.h>
#include <cutils/properties.h>
#include <utils/Log.h>

#include "IGameEventService.h"

using namespace android;

/* ./test_client <startService|stopService>
 */

int main(int argc, char **argv)
{
	int cnt;

	if (argc < 2){
		ALOGI("Usage:\n");
		ALOGI("%s <startService|stopService>\n", argv[0]);
		return -1;
	}

	/* getService */
	/* 打开驱动, mmap */
	sp<ProcessState> proc(ProcessState::self());
	/* 获得BpServiceManager */
	sp<IServiceManager> sm = defaultServiceManager();
	
	if (strcmp(argv[1], "startService") == 0)
	{
		sp<IBinder> binder = sm->getService(String16("GameEvent"));
		if (binder == 0)
		{
			ALOGI("can't get GameEvent service\n");
			return -1;
		}

		/* service肯定是BpHelloServie指针 */
		sp<IGameEventService> service =	interface_cast<IGameEventService>(binder);
		
		/* 调用Service的函数 */
		service->startService();
		ALOGI("client call startService");
	}
	else
	{
		sp<IBinder> binder = sm->getService(String16("GameEvent"));
		if (binder == 0)
		{
			ALOGI("can't get GameEvent service\n");
			return -1;
		}

		/* service肯定是BpGoodbyeServie指针 */
		sp<IGameEventService> service = interface_cast<IGameEventService>(binder);

		/* 调用Service的函数 */
		service->stopService();
		ALOGI("client call stopService");
	}

	return 0;
}



