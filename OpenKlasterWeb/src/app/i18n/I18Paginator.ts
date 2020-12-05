import {TranslateService} from "@ngx-translate/core";
import {MatPaginatorIntl} from "@angular/material/paginator";

export class I18Paginator {

  constructor(private translate: TranslateService) {}

  getPaginatorIntl(): MatPaginatorIntl {
    const paginatorIntl = new MatPaginatorIntl();
    if(navigator.language === 'pl' || navigator.language === 'pl-PL') {
      paginatorIntl.itemsPerPageLabel = 'Pozycje na stronie:';
      paginatorIntl.nextPageLabel = 'Następna strona';
      paginatorIntl.previousPageLabel = 'Poprzednia strona';
      paginatorIntl.firstPageLabel = 'Pierwsza strona';
      paginatorIntl.lastPageLabel = 'Ostatnia strona';
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
