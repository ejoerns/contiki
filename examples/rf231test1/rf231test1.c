

#include "contiki.h"

#include "hal.h"
#include "aes.h"


#include <stdio.h> /* For printf() */
/*---------------------------------------------------------------------------*/

PROCESS(aes_performance_test, "AES PERFORMANCE TEST");


    static struct etimer timer;
    unsigned long counter=0;


AUTOSTART_PROCESSES(&aes_performance_test);

/*---------------------------------------------------------------------------*/

PROCESS_THREAD(aes_performance_test, ev, data)
{

  PROCESS_BEGIN();

    printf("Process starting... ");
    uint8_t key[16]={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    uint8_t txt[16]={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    uint8_t res[16]={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    aes_setKey(key);
    etimer_set(&timer, 32*CLOCK_SECOND);
    while (!etimer_expired(&timer)){
      aes_decode(1,txt, res); //1
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res); //10
      aes_decode(1,txt, res); //11
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res); //20
      aes_decode(1,txt, res); //21
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res); //30
      aes_decode(1,txt, res); //31
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res); //40
      aes_decode(1,txt, res); //41
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res); //50
      aes_decode(1,txt, res); //51
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res); //60
      aes_decode(1,txt, res); //61
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res); //64 = 1024 Byte
      aes_decode(1,txt, res); //1
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res); //10
      aes_decode(1,txt, res); //11
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res); //20
      aes_decode(1,txt, res); //21
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res); //30
      aes_decode(1,txt, res); //31
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res); //40
      aes_decode(1,txt, res); //41
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res); //50
      aes_decode(1,txt, res); //51
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res); //60
      aes_decode(1,txt, res); //61
      aes_decode(1,txt, res);
      aes_decode(1,txt, res);
      aes_decode(1,txt, res); //64 = 2048 Byte
      counter++;
      PROCESS_PAUSE();
    }

    printf("\n2048 * %lu Byte wurden durchlaufen \n\n", counter);



  PROCESS_END();

}

/*---------------------------------------------------------------------------*/

