/*
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the Institute nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE INSTITUTE AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE INSTITUTE OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 * This file is part of the Contiki operating system.
 *
 * modified 2011 by A. Figur original file: examples\udp-ipv6\udp-client.c
 */

#include "contiki.h"
#include "contiki-lib.h"
#include "contiki-net.h"
#include "aes.h"

#include "stdio.h"
#include <string.h>

//#define DEBUG DEBUG_PRINT
//#include "net/uip-debug.h"

#define INIT_WAIT_INTERVAL_FACTOR	15
#define MAX_PAYLOAD_LEN				1024
#define SEND_INTERVAL_FACTOR		4

static struct uip_udp_conn *client_conn;
/*---------------------------------------------------------------------------*/
PROCESS(sender, "AES testsender");
AUTOSTART_PROCESSES(&sender);
/*---------------------------------------------------------------------------*/
static void
reply_handler(void)
{
  printf(".");
/*  char *str;

  if(uip_newdata()) {
    str = uip_appdata;
    str[uip_datalen()] = '\0';
    printf("Response from the server: '%s'\n", str);
  }*/
}
/*---------------------------------------------------------------------------*/
static void
init_transfer(void)
{
  char buf[MAX_PAYLOAD_LEN];
  sprintf(buf, "!Begin transfer!");
  uip_udp_packet_send(client_conn, buf, strlen(buf));
}
/*---------------------------------------------------------------------------*/
static void
print_local_addresses(void)
{
/*  int i;
  uint8_t state;

  printf("Client IPv6 addresses: ");
  for(i = 0; i < UIP_DS6_ADDR_NB; i++) {
    state = uip_ds6_if.addr_list[i].state;
    if(uip_ds6_if.addr_list[i].isused &&
       (state == ADDR_TENTATIVE || state == ADDR_PREFERRED)) {
      PRINT6ADDR(&uip_ds6_if.addr_list[i].ipaddr);
      printf("\n");
    }
  }*/
}
/*---------------------------------------------------------------------------*/
#if UIP_CONF_ROUTER
static void
set_global_address(void)
{
  uip_ipaddr_t ipaddr;

  uip_ip6addr(&ipaddr, 0xaaaa, 0, 0, 0, 0, 0, 0, 0);
  uip_ds6_set_addr_iid(&ipaddr, &uip_lladdr);
  uip_ds6_addr_add(&ipaddr, 0, ADDR_AUTOCONF);
}
#endif /* UIP_CONF_ROUTER */
/*---------------------------------------------------------------------------*/
static void
set_connection_address(uip_ipaddr_t *ipaddr)
{
#define _QUOTEME(x) #x
#define QUOTEME(x) _QUOTEME(x)
#ifdef UDP_CONNECTION_ADDR
  if(uiplib_ipaddrconv(QUOTEME(UDP_CONNECTION_ADDR), ipaddr) == 0) {
    printf("UDP client failed to parse address '%s'\n", QUOTEME(UDP_CONNECTION_ADDR));
  }
#elif UIP_CONF_ROUTER
  uip_ip6addr(ipaddr,0xaaaa,0,0,0,0x0212,0x7404,0x0004,0x0404);
#else
  uip_ip6addr(ipaddr,0xfe80,0,0,0,0x0211,0x2233,0x4455,0x6677);
#endif /* UDP_CONNECTION_ADDR */
}
/*---------------------------------------------------------------------------*/
PROCESS_THREAD(sender, ev, data)
{
  static struct etimer et;
  static struct etimer ct;
  uip_ipaddr_t ipaddr;

  PROCESS_BEGIN();
  printf("Sender started\n");

#if UIP_CONF_ROUTER
  set_global_address();
#endif

  print_local_addresses();

  set_connection_address(&ipaddr);

  /* new connection with remote host */
  client_conn = udp_new(&ipaddr, UIP_HTONS(3000), NULL);
  udp_bind(client_conn, UIP_HTONS(3001));

  printf("Created a connection with the server ");
//  PRINT6ADDR(&client_conn->ripaddr);
//  printf(" local/remote port %u/%u\n",
//	UIP_HTONS(client_conn->lport), UIP_HTONS(client_conn->rport));

  etimer_set(&et, INIT_WAIT_INTERVAL_FACTOR*CLOCK_SECOND);
  printf("Waiting %u seconds for network discovery ...\n",INIT_WAIT_INTERVAL_FACTOR);
  PROCESS_WAIT_EVENT_UNTIL(etimer_expired(&et));
  printf("done.\n Triggering Traffic ...\n");
  init_transfer();
  static int seq_id;
  etimer_set(&ct, SEND_INTERVAL_FACTOR*CLOCK_SECOND);
  while(!etimer_expired(&ct)) {
    
    PROCESS_YIELD();
    if(ev == tcpip_event) {
      reply_handler();
	  // send another packet ...
      uint8_t buf[MAX_PAYLOAD_LEN];
	  uint8_t tdata[16]={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
      static uint8_t x;
	  x=0;
      while(x<MAX_PAYLOAD_LEN/16){
        aes_encode(AES_CBC_MODE, tdata, tdata);
		buf[16*x   ]=tdata[ 0]; buf[16*x+ 1]=tdata[ 1]; buf[16*x+ 2]=tdata[ 2]; buf[16*x+ 3]=tdata[ 3];
		buf[16*x+ 4]=tdata[ 4]; buf[16*x+ 5]=tdata[ 5]; buf[16*x+ 6]=tdata[ 6]; buf[16*x+ 7]=tdata[ 7];
		buf[16*x+ 8]=tdata[ 8]; buf[16*x+ 9]=tdata[ 9]; buf[16*x+10]=tdata[10]; buf[16*x+11]=tdata[11];
		buf[16*x+12]=tdata[12]; buf[16*x+13]=tdata[13]; buf[16*x+14]=tdata[14]; buf[16*x+15]=tdata[15];
	    x++;
		aes_encode(AES_CBC_MODE, tdata, tdata);
		buf[16*x   ]=tdata[ 0]; buf[16*x+ 1]=tdata[ 1]; buf[16*x+ 2]=tdata[ 2]; buf[16*x+ 3]=tdata[ 3];
		buf[16*x+ 4]=tdata[ 4]; buf[16*x+ 5]=tdata[ 5]; buf[16*x+ 6]=tdata[ 6]; buf[16*x+ 7]=tdata[ 7];
		buf[16*x+ 8]=tdata[ 8]; buf[16*x+ 9]=tdata[ 9]; buf[16*x+10]=tdata[10]; buf[16*x+11]=tdata[11];
		buf[16*x+12]=tdata[12]; buf[16*x+13]=tdata[13]; buf[16*x+14]=tdata[14]; buf[16*x+15]=tdata[15];
	    x++;
		aes_encode(AES_CBC_MODE, tdata, tdata);
		buf[16*x   ]=tdata[ 0]; buf[16*x+ 1]=tdata[ 1]; buf[16*x+ 2]=tdata[ 2]; buf[16*x+ 3]=tdata[ 3];
		buf[16*x+ 4]=tdata[ 4]; buf[16*x+ 5]=tdata[ 5]; buf[16*x+ 6]=tdata[ 6]; buf[16*x+ 7]=tdata[ 7];
		buf[16*x+ 8]=tdata[ 8]; buf[16*x+ 9]=tdata[ 9]; buf[16*x+10]=tdata[10]; buf[16*x+11]=tdata[11];
		buf[16*x+12]=tdata[12]; buf[16*x+13]=tdata[13]; buf[16*x+14]=tdata[14]; buf[16*x+15]=tdata[15];
	    x++;
		aes_encode(AES_CBC_MODE, tdata, tdata);
		buf[16*x   ]=tdata[ 0]; buf[16*x+ 1]=tdata[ 1]; buf[16*x+ 2]=tdata[ 2]; buf[16*x+ 3]=tdata[ 3];
		buf[16*x+ 4]=tdata[ 4]; buf[16*x+ 5]=tdata[ 5]; buf[16*x+ 6]=tdata[ 6]; buf[16*x+ 7]=tdata[ 7];
		buf[16*x+ 8]=tdata[ 8]; buf[16*x+ 9]=tdata[ 9]; buf[16*x+10]=tdata[10]; buf[16*x+11]=tdata[11];
		buf[16*x+12]=tdata[12]; buf[16*x+13]=tdata[13]; buf[16*x+14]=tdata[14]; buf[16*x+15]=tdata[15];
	    x++;
	    PROCESS_PAUSE();
      }
      ++seq_id;
      uip_udp_packet_send(client_conn, buf, MAX_PAYLOAD_LEN /* strlen(buf)*/);
    }
  }
  printf("\nTotal # of %u byte packets transmitted in %u seconds : %u\n\n", MAX_PAYLOAD_LEN, SEND_INTERVAL_FACTOR, seq_id);
  PROCESS_END();
}
/*---------------------------------------------------------------------------*/
