import {TranslateService} from "@ngx-translate/core";
import {MatPaginatorIntl} from "@angular/material/paginator";

export class I18Paginator {

  constructor(private translate: TranslateService) {}

  getPaginatorIntl(): MatPaginatorIntl {
    const paginatorIntl = new MatPaginatorIntl();
    if(navigator.language === 'pl' || navigator.language === 'pl-PL') {
      paginatorIntl.itemsPerPageLabel = this.translate.instant('ITEMS_PER_PAGE_LABEL');
      paginatorIntl.nextPageLabel = this.translate.instant('NEXT_PAGE_LABEL');
      paginatorIntl.previousPageLabel = this.translate.instant('PREVIOUS_PAGE_LABEL');
      paginatorIntl.firstPageLabel = this.translate.instant('FIRST_PAGE_LABEL');
      paginatorIntl.lastPageLabel = this.translate.instant('LAST_PAGE_LABEL');
      paginatorIntl.getRangeLabel = this.getRangeLabel.bind(this);
    }
    return paginatorIntl;
  }

  private getRangeLabel(page: number, pageSize: number, length: number): string {
    if (length === 0 || pageSize === 0) {
      return this.translate.instant('RANGE_PAGE_LABEL_1', { length });
    }
    length = Math.max(length, 0);
    const startIndex = page * pageSize;
    const endIndex = startIndex < length ? Math.min(startIndex + pageSize, length) : startIndex + pageSize;
    return this.translate.instant('RANGE_PAGE_LABEL_2', { startIndex: startIndex + 1, endIndex, length });
  }
}
