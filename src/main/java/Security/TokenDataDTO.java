package Security;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenDataDTO {
    private String username;
    private String rol;
    private long fecha_creacion;
    private long fecha_expiracion;
}