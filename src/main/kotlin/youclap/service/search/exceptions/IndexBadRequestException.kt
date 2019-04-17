package youclap.service.search.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "invalid index (insert CHALLENGE or USER or both)")
class IndexBadRequestException : RuntimeException() {}