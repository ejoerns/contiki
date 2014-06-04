/*
 * Copyright (c) 2013, Swedish Institute of Computer Science.
 * All rights reserved.
 *
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
 *
 */

/**
 * \file IPv6 Neighbor cache (link-layer/IPv6 address mapping)
 *
 * \author Mathilde Durvy <mdurvy@cisco.com>
 * \author Julien Abeille <jabeille@cisco.com>
 * \author Simon Duquennoy <simonduq@sics.se>
 */

/**
 * \addtogroup uip6
 * @{
 */

/** @defgroup uip_ds6_nbr IPv6 Neighbor cache (link-layer/IPv6 address mapping)
 *
 * Data structure and methods for IPv6 Neighbor Cache (RFC 4861, section 5.1)
 *
 * \author Mathilde Durvy <mdurvy@cisco.com>
 * \author Julien Abeille <jabeille@cisco.com>
 * \author Simon Duquennoy <simonduq@sics.se>
 * @{ */

#ifndef UIP_DS6_NEIGHBOR_H_
#define UIP_DS6_NEIGHBOR_H_

#include "net/ip/uip.h"
#include "net/nbr-table.h"
#include "sys/stimer.h"
#include "net/ipv6/uip-ds6.h"
#include "net/nbr-table.h"

#if UIP_CONF_IPV6_QUEUE_PKT
#include "net/ip/uip-packetqueue.h"
#endif /* UIP_CONF_IPV6_QUEUE_PKT */

/*--------------------------------------------------*/
/** \name Possible states for the nbr cache entries
 * @{ */
#define  NBR_INCOMPLETE 0
#define  NBR_REACHABLE  1
#define  NBR_STALE      2
#define  NBR_DELAY      3
#define  NBR_PROBE      4
/** @} */

NBR_TABLE_DECLARE(ds6_neighbors);

/** \brief An entry in the nbr cache */
typedef struct uip_ds6_nbr {
  uip_ipaddr_t ipaddr;
  struct stimer reachable;
  struct stimer sendns;
  uint8_t nscount;
  uint8_t isrouter;
  uint8_t state;
#if UIP_CONF_IPV6_QUEUE_PKT
  struct uip_packetqueue_handle packethandle;
#define UIP_DS6_NBR_PACKET_LIFETIME CLOCK_SECOND * 4
#endif /* UIP_CONF_IPV6_QUEUE_PKT */
} uip_ds6_nbr_t;

/** Initialize neighbor cache. */
void uip_ds6_neighbors_init(void);

/** \name Neighbor Cache basic routines
 *
 * @{ */
/** Adds a neighbor to the neighbor cache. 
 *
 * @param ipaddr IP address of the neighbor to add.
 * @param lladdr Link-local address of the neighbor (may be unknown when added),
 *        NULL if not known (requires state to be set to NBR_INCOMPLETE)
 * @param isrouter Set to 1 if neighbor is a router,
 *        to 0 if host or unknown
 * @param state State of this entry.
 *        Possible values are:
 *        \ref NBR_INCOMPLETE,
 *        \ref NBR_REACHABLE,
 *        \ref NBR_STALE,
 *        \ref NBR_DELAY,
 *        \ref NBR_PROBE
 */
uip_ds6_nbr_t *uip_ds6_nbr_add(const uip_ipaddr_t *ipaddr, const uip_lladdr_t *lladdr,
                               uint8_t isrouter, uint8_t state);
/** Removes entry from neighbor cache.
 */
void uip_ds6_nbr_rm(uip_ds6_nbr_t *nbr);
/** Returns link-local address of neighbor.
 */
const uip_lladdr_t *uip_ds6_nbr_get_ll(const uip_ds6_nbr_t *nbr);
/** Returns IP address of neighbor.
 */
const uip_ipaddr_t *uip_ds6_nbr_get_ipaddr(const uip_ds6_nbr_t *nbr);
/** Lookup if a neighbor cache entry for given IP address exists.
 *
 * @param ipaddr IP address to look up
 * @return NULL if no entry was found,
 *        otherwise a pointer to that entry
 */
uip_ds6_nbr_t *uip_ds6_nbr_lookup(const uip_ipaddr_t *ipaddr);
/** Lookup if a neighbor cache entry for given link-layer address exists.
 *
 * @param llpaddr link-layer address to look up
 * @return NULL if no entry was found,
 *        otherwise a pointer to that entry
 */
uip_ds6_nbr_t *uip_ds6_nbr_ll_lookup(const uip_lladdr_t *lladdr);
/** Returns IP address associated with link-local address, based on neighbor cache entry. */
uip_ipaddr_t *uip_ds6_nbr_ipaddr_from_lladdr(const uip_lladdr_t *lladdr);
/** Returns link-layer address associated with IP address, based on neighbor cache entry. */
const uip_lladdr_t *uip_ds6_nbr_lladdr_from_ipaddr(const uip_ipaddr_t *ipaddr);
/** ? */
void uip_ds6_link_neighbor_callback(int status, int numtx);
/** ? */
void uip_ds6_neighbor_periodic(void);
/** ? */
int uip_ds6_nbr_num(void);

/**
 * \brief
 * This searches inside the neighbor table for the neighbor that is about to
 * expire the next.
 *
 * @return
 * A reference to the neighbor about to expire the next or NULL if
 * table is empty.
 */
uip_ds6_nbr_t *uip_ds6_get_least_lifetime_neighbor(void);
/** @} */

/** @} */

#endif /* UIP_DS6_NEIGHBOR_H_ */
