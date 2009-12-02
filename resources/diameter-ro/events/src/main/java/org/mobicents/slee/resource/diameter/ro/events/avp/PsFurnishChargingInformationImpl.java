package org.mobicents.slee.resource.diameter.ro.events.avp;

import net.java.slee.resource.diameter.ro.events.avp.PsAppendFreeFormatData;
import net.java.slee.resource.diameter.ro.events.avp.PsFurnishChargingInformation;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * PsFurnishChargingInformationImpl.java
 *
 * <br>Project:  mobicents
 * <br>12:46:28 PM Apr 13, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class PsFurnishChargingInformationImpl extends GroupedAvpImpl implements PsFurnishChargingInformation {

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public PsFurnishChargingInformationImpl( int code, long vendorId, int mnd, int prt, byte[] value ) {
    super( code, vendorId, mnd, prt, value );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsFurnishChargingInformation#getPsAppendFreeFormatData()
   */
  public PsAppendFreeFormatData getPsAppendFreeFormatData() {
    return (PsAppendFreeFormatData) getAvpAsEnumerated(DiameterRoAvpCodes.PS_APPEND_FREE_FORMAT_DATA, DiameterRoAvpCodes.TGPP_VENDOR_ID, PsAppendFreeFormatData.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsFurnishChargingInformation#getPsFreeFormatData()
   */
  public String getPsFreeFormatData() {
    return getAvpAsOctetString(DiameterRoAvpCodes.PS_FREE_FORMAT_DATA, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsFurnishChargingInformation#getTgppChargingId()
   */
  public String getTgppChargingId() {
    return getAvpAsOctetString(DiameterRoAvpCodes.TGPP_CHARGING_ID, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsFurnishChargingInformation#hasPsAppendFreeFormatData()
   */
  public boolean hasPsAppendFreeFormatData() {
    return hasAvp( DiameterRoAvpCodes.PS_APPEND_FREE_FORMAT_DATA, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsFurnishChargingInformation#hasPsFreeFormatData()
   */
  public boolean hasPsFreeFormatData() {
    return hasAvp( DiameterRoAvpCodes.PS_FREE_FORMAT_DATA, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsFurnishChargingInformation#hasTgppChargingId()
   */
  public boolean hasTgppChargingId() {
    return hasAvp( DiameterRoAvpCodes.TGPP_CHARGING_ID, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsFurnishChargingInformation#setPsAppendFreeFormatData(net.java.slee.resource.diameter.ro.events.avp.PsAppendFreeFormatData)
   */
  public void setPsAppendFreeFormatData( PsAppendFreeFormatData psAppendFreeFormatData ) {
    addAvp(DiameterRoAvpCodes.PS_APPEND_FREE_FORMAT_DATA, DiameterRoAvpCodes.TGPP_VENDOR_ID, psAppendFreeFormatData.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsFurnishChargingInformation#setPsFreeFormatData(byte[])
   */
  public void setPsFreeFormatData( String psFreeFormatData ) {
    addAvp(DiameterRoAvpCodes.PS_FREE_FORMAT_DATA, DiameterRoAvpCodes.TGPP_VENDOR_ID, psFreeFormatData);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsFurnishChargingInformation#setTgppChargingId(byte[])
   */
  public void setTgppChargingId( String tgppChargingId ) {
    addAvp(DiameterRoAvpCodes.TGPP_CHARGING_ID, DiameterRoAvpCodes.TGPP_VENDOR_ID, tgppChargingId);
  }

}
