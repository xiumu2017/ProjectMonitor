
            // 判断是否开启发送 -- 一下判断改为前端，调用方自行处理
            if (config.getSendAble().equals(ProjectConstant.SMS_SEND_ENABLE)) {
                // 判断是否在发送区间
                int hour = Calendar.getInstance().get(Calendar.HOUR);
                if (config.getStartHour() <= hour && hour < config.getEndHour()) {
                    if (config.getSendFlag().equals(ProjectConstant.SMS_LIMIT_COUNT_TRUE)) {
                        // 判断发送量情况
                        // 1. 是否超出月发送量
                        if (config.getSendTotalCount() > config.getSendMonth()) {

                        }
                        // 2. 是否超出日发送量
                        if (config.getSendCount() > config.getSendDay()) {
                        }
                    }

                }
            }