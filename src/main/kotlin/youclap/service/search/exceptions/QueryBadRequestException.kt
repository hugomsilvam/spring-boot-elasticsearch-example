package youclap.service.search.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Query is not present :D")
class QueryBadRequestException : RuntimeException() {}