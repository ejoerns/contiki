/**
 * Define en-/decryption values to be used in an app while calling
 * aes_encode() or aes_decode() as mode parameter
 */
#define AES_ECB_MODE	0
#define AES_CBC_MODE	1

/**
 *  Sets the key for en-/decoding
 *
 *  key    - Keyarray (16-byte)
 *
 *  return - 0 if all ok, 1 if error occurred (i.e. RF231 chip not active)
 */
uint8_t aes_setKey(uint8_t key[]);

/**
 *  Performs encryption
 *
 *  mode   - Sets encryption mode (1=CBC, 0=ECB, 2+=reserved)
 *  txt    - Array of 16 byte to encode
 *  ctxt   - Array to store the result of the last encryption in (16 byte)
 *
 *  return - 0 if all ok, 1 if error occurred while encrypting, 2 if codeerror occured 
 */
uint8_t aes_encode(uint8_t mode, uint8_t txt[], uint8_t ctxt[]);

/**
 *  Performs decryption
 *
 *  mode   - Sets decryption mode (1=CBC, 0=ECB, 2+=reserved)
 *  ctxt   - Array of 16 byte to decode
 *  txt    - Array to store the result of the last decryption in (16 byte)
 *
 *  return - 0 if all ok, 1 if error occurred while encrypting, 2 if codeerror occured 
 */
uint8_t aes_decode(uint8_t mode, uint8_t ctxt[], uint8_t txt[]);


/**
 *  Method to determine the variant of AES used
 *
 *  return - 0  if empty functions
 *           1  if slow SW-AES
 *           2  if fast SW-AES
 *           3  if RF-231 HW AES
 *           4+ reserved
 */
uint8_t aesVer();