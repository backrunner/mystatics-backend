package top.backrunner.installstat.utils.application;

import top.backrunner.installstat.app.entity.VersionInfo;
import top.backrunner.installstat.app.exception.BranchNotEqualException;

import java.util.List;

public class VersionUtils {

    public static short compare(VersionInfo a, VersionInfo b) throws BranchNotEqualException {
        if (!a.getBranch().equals(b.getBranch())){
            throw new BranchNotEqualException();
        }
        String[] ver_a = a.getVersion().split(".");
        String[] ver_b = b.getVersion().split(".");
        if (Integer.parseInt(ver_a[0]) > Integer.parseInt(ver_b[0])){
            return 1;
        } else {
            if (Integer.parseInt(ver_a[1]) > Integer.parseInt(ver_b[1])){
                return 1;
            } else {
                if (Integer.parseInt(ver_a[2]) > Integer.parseInt(ver_b[2])){
                    return 1;
                } else {
                    if (Integer.parseInt(ver_a[2]) == Integer.parseInt(ver_b[2])){
                        return 0;
                    } else {
                        return -1;
                    }
                }
            }
        }
    }
    public static short compare(String a, String b){
        String[] ver_a = a.split(".");
        String[] ver_b = b.split(".");
        if (Integer.parseInt(ver_a[0]) > Integer.parseInt(ver_b[0])){
            return 1;
        } else {
            if (Integer.parseInt(ver_a[1]) > Integer.parseInt(ver_b[1])){
                return 1;
            } else {
                if (Integer.parseInt(ver_a[2]) > Integer.parseInt(ver_b[2])){
                    return 1;
                } else {
                    if (Integer.parseInt(ver_a[2]) == Integer.parseInt(ver_b[2])){
                        return 0;
                    } else {
                        return -1;
                    }
                }
            }
        }
    }

    public static String max(List<VersionInfo> versions, String branch){
        if (versions == null || versions.isEmpty()){
            return null;
        }
        String maxVersion = null;
        int max_major = 0;
        int max_minor = 0;
        int max_patch = 0;
        for (VersionInfo v : versions){
            // 跳过分支不匹配的版本
            if (!branch.equals(v.getBranch())){
                continue;
            }
            String[] ver = v.getVersion().split(".");
            if (Integer.parseInt(ver[0]) > max_major){
                max_major = Integer.parseInt(ver[0]);
                maxVersion = v.getVersion();
            } else {
                if (Integer.parseInt(ver[1]) > max_minor){
                    max_minor = Integer.parseInt(ver[1]);
                    maxVersion = v.getVersion();
                } else {
                    if (Integer.parseInt(ver[2]) > max_patch){
                        max_patch = Integer.parseInt(ver[2]);
                        maxVersion = v.getVersion();
                    }
                }
            }
        }
        return maxVersion;
    }

    public static boolean validate(String version){
        return version.matches("^[0-9]+\\.[0-9]+\\.[0-9]+$");
    }
}
