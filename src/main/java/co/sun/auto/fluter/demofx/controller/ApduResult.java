package co.sun.auto.fluter.demofx.controller;

public class ApduResult {
    public boolean isSuccess = false;
    public byte[] response = null;

    public ApduResult(boolean isSuccess, byte[] response) {
        this.isSuccess = isSuccess;
        this.response = response;
    }
}
